package mit.iwrcore.IWRCore.security.service;



import mit.iwrcore.IWRCore.entity.FileMaterial;
import mit.iwrcore.IWRCore.entity.Material;


import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;

import java.util.List;

public interface MaterialService {

    Long saveMaterial(MaterialDTO dto, List<FileMaterial> fileList);
    void deleteMaterial(Long materCode);

    // 자재 찾기
    MaterialDTO findM (Long matercode);
    // 자재 리스트
    PageResultDTO<MaterialDTO, Material> findMaterialAll(PageRequestDTO requestDTO);//모든 리스트
    // 제품등록 중 자재들
    PageResultDTO<MaterialDTO, Material> productMaterialList(PageRequestDTO requestDTO);//모든 리스트

    List<Material> findMaterialPart(Long boxcode, Long materscode); //일부분(창고별, 자재소분류별)
    List<MaterialDTO> materialList();


    Material materdtoToEntity(MaterialDTO dto);
    MaterialDTO materTodto(Material entity);
}
