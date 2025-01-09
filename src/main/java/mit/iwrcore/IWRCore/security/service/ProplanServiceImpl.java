package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.ProPlanRepository;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanContractNumDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanSturctureDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProplanServiceImpl implements ProplanService{
    private final ProPlanRepository proPlanRepository;
    private final ProductService productService;
    private final MemberService memberService;
    private final LineService lineService;
    private final StructureService structureService;

    // 저장, 삭제
    @Override
    public ProplanDTO saveProPlan(ProplanDTO dto, List<FileProPlan> fileList) {
        ProPlan proPlan = dtoToEntity(dto);
        proPlan.setFiles(fileList);
        ProPlan savedProPlan=proPlanRepository.save(proPlan);
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
        ProPlan entity=ProPlan.builder()
                .proplanNo(dto.getProplanNo())
                .pronum(dto.getPronum())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .details(dto.getDetails())
                .finCheck(dto.getFinCheck())
                .product(productService.dtoToEntity(dto.getProductDTO()))
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO()))
                .build();
        return entity;
    }
    @Override
    public ProplanDTO entityToDTO(ProPlan entity) {
        ProplanDTO dto=ProplanDTO.builder()
                .proplanNo(entity.getProplanNo())
                .pronum(entity.getPronum())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .details(entity.getDetails())
                .finCheck(entity.getFinCheck())
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


    // 생산부서> 모든 생산계획 목록
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
    // 생산부서> 진행중인 생산계획 목록
    @Override
    public PageResultDTO<ProplanDTO, ProPlan> getNotFinProPlan(PageRequestDTO requestDTO) {
        Page<ProPlan> entityPage=proPlanRepository.getPlanProPlan(requestDTO);
        Function<ProPlan, ProplanDTO> fn = (entity -> entityToDTO(entity));
        return new PageResultDTO<>(entityPage, fn);
    }
}

