package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Contract;
import mit.iwrcore.IWRCore.entity.FileProPlan;
import mit.iwrcore.IWRCore.entity.ProPlan;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.ProplanDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanContractNumDTO;

import java.util.List;

public interface ProplanService {
    // 저장, 삭제
    ProplanDTO saveProPlan(ProplanDTO dto, List<FileProPlan> fileList);
    void update(ProplanDTO dto);
    void deleteById(Long id);

    // 변환
    ProPlan dtoToEntity(ProplanDTO dto);
    ProplanDTO entityToDTO(ProPlan entity);

    // 조회
    ProplanDTO getProPlan(Long id);
    Long checkProPlan(Long manuCode);


    // 생산부서> 모든 생산계획 목록
    PageResultDTO<ProPlanContractNumDTO, Object[]> getAllProPlans(PageRequestDTO2 requestDTO);
    // 생산부서> 진행중인 생산계획 목록
    PageResultDTO<ProplanDTO, ProPlan> getNotFinProPlan(PageRequestDTO requestDTO);
//    // 생산부서> 생산계획에 따른 제품구성 및 수량
//    List<ProPlanSturctureDTO> getStructureStock(Long proplanNo);
}