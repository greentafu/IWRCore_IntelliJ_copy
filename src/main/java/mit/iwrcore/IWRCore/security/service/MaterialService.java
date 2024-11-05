package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.FileMaterial;
import mit.iwrcore.IWRCore.entity.Material;

import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;

import java.util.List;

public interface MaterialService {
    // 저장, 삭제
    MaterialDTO saveMaterial(MaterialDTO dto, List<FileMaterial> fileList);
    void deleteMaterial(Long materCode);

    // 변환
    Material dtoToEntity(MaterialDTO dto);
    MaterialDTO entityToDto(Material entity);

    // 조회
    MaterialDTO getMaterial(Long matercode);


    // 자재관리> 모든 자재 목록
    PageResultDTO<MaterialDTO, Material> findMaterialAll(PageRequestDTO requestDTO);//모든 리스트
    // 제품등록> 제품에 선택되지 않은 자재 목록
    PageResultDTO<MaterialDTO, Material> productMaterialList(PageRequestDTO requestDTO);//모든 리스트
}
