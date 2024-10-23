package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.FileMaterial;
import mit.iwrcore.IWRCore.entity.FileProPlan;
import mit.iwrcore.IWRCore.entity.ProPlan;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.ProplanDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanContractNumDTO;

import java.util.List;

public interface ProplanService {
    ProplanDTO saveProPlan(ProplanDTO dto, List<FileProPlan> fileList);
    void update(ProplanDTO dto);
    void deleteById(Long id);
    ProplanDTO findById(Long id);
    PageResultDTO<ProplanDTO, ProPlan> proplanList(PageRequestDTO2 requestDTO);
    PageResultDTO<ProPlanContractNumDTO, Object[]> proplanList2(PageRequestDTO2 requestDTO);
//    List<ProplanDTO> findByPlanId(Long planId);

    ProPlan dtoToEntity(ProplanDTO dto);
    ProplanDTO entityToDTO(ProPlan entity);

    Long checkProPlan(Long manuCode);

}