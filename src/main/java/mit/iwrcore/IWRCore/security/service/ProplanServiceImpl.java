package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.FileProPlan;
import mit.iwrcore.IWRCore.entity.Line;
import mit.iwrcore.IWRCore.entity.ProPlan;
import mit.iwrcore.IWRCore.repository.ProPlanRepository;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.ProplanDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanContractNumDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProplanServiceImpl implements ProplanService{
    private final ProPlanRepository proPlanRepository;
    private final ProductService productService;
    private final MemberService memberService;
    private final LineService lineService;

    // 저장, 삭제
    @Override
    public ProplanDTO saveProPlan(ProplanDTO dto, List<FileProPlan> fileList) {
        ProPlan proPlan = dtoToEntity(dto);
        proPlan.setFiles(fileList);
        ProPlan savedProPlan=proPlanRepository.save(proPlan); // savedProPlan = 저장된 proplan
        return entityToDTO(savedProPlan);
    }
    @Override
    public void update(ProplanDTO dto) {
        ProPlan proPlan = dtoToEntity(dto);
        proPlanRepository.save(proPlan);
    }
    @Override
    public void deleteById(Long id) {
        proPlanRepository.deleteById(id);
    }

    // 변환
    @Override
    public ProPlan dtoToEntity(ProplanDTO dto) {
        List<String> strings=dto.getLine();
        List<Line> lines=new ArrayList<>();
        strings.forEach(x->lines.add(lineService.stringToLine(x)));
        ProPlan entity=ProPlan.builder()
                .proplanNo(dto.getProplanNo())
                .pronum(dto.getPronum())
                .filename(dto.getFilename())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .line(lines)
                .details(dto.getDetails())
                .product(productService.dtoToEntity(dto.getProductDTO()))
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO()))
                .build();
        return entity;
    }
    @Override
    public ProplanDTO entityToDTO(ProPlan entity) {
        List<Line> lines=entity.getLine();
        List<String> strings=new ArrayList<>();
        lines.forEach(x->strings.add(x.getLineName()));
        ProplanDTO dto=ProplanDTO.builder()
                .proplanNo(entity.getProplanNo())
                .pronum(entity.getPronum())
                .filename(entity.getFilename())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .line(strings)
                .details(entity.getDetails())
                .regDate(entity.getRegDate())
                .productDTO(productService.entityToDto(entity.getProduct()))
                .memberDTO(memberService.memberTodto(entity.getWriter()))
                .build();
        return dto;
    }

    // 조회
    @Override
    public ProplanDTO getProPlan(Long id) {
        ProPlan proPlan=proPlanRepository.findById(id).get();
        return entityToDTO(proPlan);
    }
    @Override
    public Long checkProPlan(Long manuCode){
        return proPlanRepository.checkProPlan(manuCode);
    }


    // 생산팀> 모든 생산계획 목록
    @Override
    public PageResultDTO<ProPlanContractNumDTO, Object[]> getAllProPlans(PageRequestDTO2 requestDTO){
        Page<Object[]> entityPage=proPlanRepository.findProPlanByCustomQuery(requestDTO);
        return new PageResultDTO<>(entityPage, this::exProplan);
    }
    private ProPlanContractNumDTO exProplan(Object[] objects){
        ProPlan proPlan=(ProPlan) objects[0];
        Long jcnum=(Long) objects[2];
        Long contractNum=(Long) objects[1];
        ProplanDTO proplanDTO=(proPlan!=null)?entityToDTO(proPlan):null;
        return new ProPlanContractNumDTO(proplanDTO, (jcnum!=null)?jcnum:0L, (contractNum!=null)?contractNum:0L);
    }
}

