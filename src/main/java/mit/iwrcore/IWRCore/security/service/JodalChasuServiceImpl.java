package mit.iwrcore.IWRCore.security.service;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.JodalChasu;
import mit.iwrcore.IWRCore.entity.JodalPlan;
import mit.iwrcore.IWRCore.entity.Structure;
import mit.iwrcore.IWRCore.repository.JodalChasuRepository;

import mit.iwrcore.IWRCore.security.dto.JodalChasuDTO;
import mit.iwrcore.IWRCore.security.dto.JodalPlanDTO;
import mit.iwrcore.IWRCore.security.dto.ProplanDTO;
import mit.iwrcore.IWRCore.security.dto.StructureDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanSturctureDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class JodalChasuServiceImpl implements JodalChasuService {
    private final JodalChasuRepository jodalChasuRepository;
    private final MemberService memberService;
    private final JodalPlanService jodalPlanService;
    private final StructureService structureService;

    // 저장, 삭제
    @Override
    public JodalChasuDTO saveJodalChasu(JodalChasuDTO dto) {
        JodalChasu entity = dtoToEntity(dto);
        JodalChasu savedEntity = jodalChasuRepository.save(entity);
        return entityToDTO(savedEntity);
    }
    @Override
    public void deleteJodalChasu(Long id) {
        jodalChasuRepository.deleteById(id);
    }
    @Override
    public void deleteJodalChasuByPlan(Long joNo){
        List<JodalChasu> list=jodalChasuRepository.getJodalChausFromPlan(joNo);
        list.forEach(x->deleteJodalChasu(x.getJcnum()));
    }

    // 변환
    @Override
    public JodalChasu dtoToEntity(JodalChasuDTO dto) {
        return JodalChasu.builder()
                .jcnum(dto.getJcnum())
                .joNum(dto.getJoNum())
                .joDate(dto.getJoDate())
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO()))
                .jodalPlan(jodalPlanService.dtoToEntity(dto.getJodalPlanDTO()))
                .build();
    }
    @Override
    public JodalChasuDTO entityToDTO(JodalChasu entity) {
        return JodalChasuDTO.builder()
                .jcnum(entity.getJcnum())
                .joNum(entity.getJoNum())
                .joDate(entity.getJoDate())
                .memberDTO(memberService.memberTodto(entity.getWriter()))
                .jodalPlanDTO(jodalPlanService.entityToDTO(entity.getJodalPlan()))
                .build();
    }

    // 조회
    @Override
    public List<JodalChasuDTO> getJodalChasuByJodalPlan(Long joNo){
        List<JodalChasu> entityList=jodalChasuRepository.findJCfromJP(joNo);
        List<JodalChasuDTO> dtoList=entityList.stream().map(this::entityToDTO).toList();
        return dtoList;
    }


    // 조달계획> 조달차수 리스트
    @Override
    public List<ProPlanSturctureDTO> modifyJodalChasu(Long proplanNo) {
        List<Object[]> list = jodalChasuRepository.modifyJodalChasu(proplanNo);
        return list.stream().map(this::proPlanSturctureToDTO).toList();
    }
    private ProPlanSturctureDTO proPlanSturctureToDTO(Object[] objects) {
        JodalPlan jodalPlan = (JodalPlan) objects[0];
        Structure structure = (Structure) objects[1];
        Long tempSumRequest = (Long) objects[2];
        Long tempSumShip = (Long) objects[3];
        JodalChasu jodalChasu=(JodalChasu) objects[4];
        Long countContract=(Long) objects[5];

        System.out.println(jodalPlan.getJoNo()+":"+countContract);

        StructureDTO structureDTO = (structure != null) ? structureService.entityToDto(structure) : null;
        Long sumRequest = (tempSumRequest != null) ? tempSumRequest : 0L;
        Long sumShip = (tempSumShip != null) ? tempSumShip : 0L;
        JodalPlanDTO jodalPlanDTO = (jodalPlan != null) ? jodalPlanService.entityToDTO(jodalPlan) : null;
        System.out.println(jodalPlanDTO);
        ProplanDTO proplanDTO = (jodalPlanDTO != null) ? jodalPlanDTO.getProplanDTO() : null;
        JodalChasuDTO jodalChasuDTO=(jodalChasu!=null) ? entityToDTO(jodalChasu):null;
        Long sumContract=(countContract!=null)?countContract:0L;

        return new ProPlanSturctureDTO(proplanDTO, structureDTO, sumRequest, sumShip, jodalPlanDTO, jodalChasuDTO, sumContract);
    }
}