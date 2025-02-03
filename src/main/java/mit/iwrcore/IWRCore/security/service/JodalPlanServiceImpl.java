package mit.iwrcore.IWRCore.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.JodalPlanRepository;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.JodalPlanJodalChsuDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.LLLSDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanSturctureDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class JodalPlanServiceImpl implements JodalPlanService {
    private final JodalPlanRepository jodalPlanRepository;
    private final MemberService memberService;
    private final ProplanService proplanService;
    private final MaterialService materialService;
    private final StructureService structureService;

    // 저장, 삭제
    @Override
    public void saveJodalPlan(JodalPlanDTO dto) {
        JodalPlan jodalPlan = dtoToEntity(dto);
        jodalPlanRepository.save(jodalPlan);
    }
        // Proplan 받으면 해당 제품의 구성(자재)이 조달계획 저장됨
        // 조달차수가 존재하면 조달계획 등록한 거고, 조달차수가 존재하지 않으면 조달계획 등록 하지 않은 것
    @Override
    public void saveJodalPlanFromProplan(ProplanDTO proplanDTO, MemberDTO memberDTO) {
        Long product_id = proplanDTO.getProductDTO().getManuCode();
        List<StructureDTO> structureDTOS = structureService.getStructureByProduct(product_id);
        structureDTOS.forEach(x -> {
            JodalPlanDTO jodalPlanDTO = JodalPlanDTO.builder()
                    .memberDTO(memberDTO)
                    .proplanDTO(proplanDTO)
                    .materialDTO(x.getMaterialDTO())
                    .build();
            saveJodalPlan(jodalPlanDTO);
        });
    }
    @Override
    public void deleteJodalPlan(Long id) {
        jodalPlanRepository.deleteById(id);
    }

    // 변환
    @Override
    public JodalPlan dtoToEntity(JodalPlanDTO dto) {
        return JodalPlan.builder()
                .joNo(dto.getJoNo())
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO()))
                .proPlan(proplanService.dtoToEntity(dto.getProplanDTO()))
                .material(materialService.dtoToEntity(dto.getMaterialDTO()))
                .build();
    }
    @Override
    public JodalPlanDTO entityToDTO(JodalPlan entity) {
        return JodalPlanDTO.builder()
                .joNo(entity.getJoNo())
                .memberDTO(memberService.memberTodto(entity.getWriter()))
                .proplanDTO(proplanService.entityToDTO(entity.getProPlan()))
                .materialDTO(materialService.entityToDto(entity.getMaterial()))
                .build();
    }

    // 조회
    @Override
    public JodalPlanDTO getJodalPlan(Long id) {
        JodalPlanDTO jodalPlanDTO=null;
        if(id!=null) {
            JodalPlan jodalPlan=jodalPlanRepository.findById(id).get();
            jodalPlanDTO=entityToDTO(jodalPlan);
        }
        return jodalPlanDTO;
    }
    @Override
    public Long newNoneJodalChasuCount(){
        Long count=jodalPlanRepository.newNoneJodalPlanCount();
        return (count!=null)?count:0L;
    }
    @Override
    public List<JodalPlanDTO> getJodalPlanByProPlan(Long proplanNo){
        List<JodalPlan> entityList=jodalPlanRepository.findByProPlanProplanNo(proplanNo);
        List<JodalPlanDTO> dtoList=entityList.stream().map(this::entityToDTO).toList();
        return dtoList;
    }
    @Override
    public List<JodalPlanDTO> getJodalPlanByProPlanMaterial(Long proplanNo, Long materCode){
        List<JodalPlan> entityList=jodalPlanRepository.findByProPlanMaterial(proplanNo, materCode);
        return entityList.stream().map(x->entityToDTO(x)).toList();
    }


    // 생산부서> 생산계획에 따른 제품구성 및 수량
    @Override
    public List<ProPlanSturctureDTO> getStructureStock(Long proplanNo){
        List<Object[]> list = jodalPlanRepository.getStructureStock(proplanNo);
        return list.stream().map(this::proPlanSturctureToDTO).toList();
    }
    private ProPlanSturctureDTO proPlanSturctureToDTO(Object[] objects) {
        ProPlan proPlan = (ProPlan) objects[0];
        Structure structure = (Structure) objects[1];
        JodalPlan jodalPlan = (JodalPlan) objects[2];
        Long tempSumShip = (Long) objects[3];
        Long tempSumRequest = (Long) objects[4];

        ProplanDTO proplanDTO = (proPlan != null) ? proplanService.entityToDTO(proPlan) : null;
        StructureDTO structureDTO = (structure != null) ? structureService.entityToDto(structure) : null;
        JodalPlanDTO jodalPlanDTO = (jodalPlan !=null) ? entityToDTO(jodalPlan) : null;
        Long sumShip = (tempSumShip != null) ? tempSumShip : 0L;
        Long sumRequest = (tempSumRequest != null) ? tempSumRequest : 0L;

        return new ProPlanSturctureDTO(proplanDTO, structureDTO, sumRequest, sumShip, jodalPlanDTO, null, null);
    }
    // 자재부서> 조달계획 필요 자재 목록
    @Override
    public PageResultDTO<JodalPlanDTO, JodalPlan> nonJodalplanMaterial2(PageRequestDTO requestDTO){
        Page<JodalPlan> entityPage = jodalPlanRepository.findJodalPlanByCustomQuery(requestDTO);
        Function<JodalPlan, JodalPlanDTO> fn = (entity -> entityToDTO(entity));
        return new PageResultDTO<>(entityPage, fn);
    }
    // 계약서> 계약하지 않은 조달계획 목록
    @Override
    public PageResultDTO<JodalPlanJodalChsuDTO, Object[]> noneContractJodalPlan(PageRequestDTO2 requestDTO2){
        Page<Object[]> entityPage = jodalPlanRepository.noneContractJodalPlan(requestDTO2);
        return new PageResultDTO<>(entityPage, this::exJodalPlanJodalChsuDTO);
    }
    // 계약서> 선택한 조달계획
    @Override
    public JodalPlanJodalChsuDTO selectedJodalPlan(Long joNo){
        List<Object[]> entity= jodalPlanRepository.selectedJodalPlan(joNo);
        return exJodalPlanJodalChsuDTO(entity.get(0));
    }
    private JodalPlanJodalChsuDTO exJodalPlanJodalChsuDTO(Object[] objects){
        JodalPlan jodalPlan=(JodalPlan) objects[0];
        Long allJodalChasuNum=(Long) objects[1];

        JodalPlanDTO jodalPlanDTO=(jodalPlan!=null)? entityToDTO(jodalPlan):null;
        Long allNum=(allJodalChasuNum!=null)?allJodalChasuNum:0L;
        return new JodalPlanJodalChsuDTO(jodalPlanDTO, allNum);
    }

}
