package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Material;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.entity.Structure;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
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
    List<StructureDTO> findByProduct_ManuCode(Long manuCode);




}

