package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Structure;
import mit.iwrcore.IWRCore.security.dto.StructureDTO;

import java.util.List;

public interface StructureService {
    // 저장, 삭제
    void saveStructure(StructureDTO dto);
    void deleteStructure(Long id);
    // 변환
    Structure dtoToEntity(StructureDTO dto);
    StructureDTO entityToDto(Structure entity);

    // 조회
    StructureDTO getStructure(Long sno);
    List<StructureDTO> getStructureByProduct(Long manuCode);
    List<StructureDTO> getStructureByProductMaterial(Long manuCode, Long materCode);
}

