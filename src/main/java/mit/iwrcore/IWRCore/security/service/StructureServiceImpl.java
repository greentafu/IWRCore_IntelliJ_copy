package mit.iwrcore.IWRCore.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.Structure;
import mit.iwrcore.IWRCore.repository.StructureRepository;
import mit.iwrcore.IWRCore.security.dto.StructureDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class StructureServiceImpl implements StructureService {
    private final StructureRepository structureRepository;
    private final ProductService productService;
    private final MaterialService materialService;

    // 저장, 삭제
    @Override
    public void saveStructure(StructureDTO dto) {
        Structure structure = dtoToEntity(dto); // DTO를 엔티티로 변환
        structureRepository.save(structure);    // 엔티티를 저장
    }
    @Override
    public void deleteStructure(Long id) {
        structureRepository.deleteById(id);
    }

    // 변환
    @Override
    public Structure dtoToEntity(StructureDTO dto){
        Structure entity=Structure.builder()
                .sno(dto.getSno())
                .material(materialService.dtoToEntity(dto.getMaterialDTO()))
                .product(productService.dtoToEntity(dto.getProductDTO()))
                .quantity(dto.getQuantity())
                .build();
        return entity;
    }
    @Override
    public StructureDTO entityToDto(Structure entity){
        StructureDTO dto=StructureDTO.builder()
                .sno(entity.getSno())
                .productDTO(productService.entityToDto(entity.getProduct()))
                .materialDTO(materialService.entityToDto(entity.getMaterial()))
                .quantity(entity.getQuantity())
                .build();
        return dto;
    }

    // 조회
    @Override
    public StructureDTO getStructure(Long sno){
        Structure structure=structureRepository.findById(sno).get();
        return entityToDto(structure);
    };
    @Override
    public List<StructureDTO> getStructureByProduct(Long manuCode) {
        List<Structure> list=structureRepository.findByProduct_ManuCode(manuCode);
        return list.stream().map(x->entityToDto(x)).toList();
    }
    @Override
    public List<StructureDTO> getStructureByProductMaterial(Long manuCode, Long materCode) {
        List<Structure> list=structureRepository.findByProductMaterial(manuCode, materCode);
        return list.stream().map(x->entityToDto(x)).toList();
    }
}
