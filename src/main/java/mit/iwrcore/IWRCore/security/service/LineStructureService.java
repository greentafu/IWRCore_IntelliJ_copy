package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.Line;
import mit.iwrcore.IWRCore.entity.LineStructure;
import mit.iwrcore.IWRCore.repository.LineStructureRepository;
import mit.iwrcore.IWRCore.security.dto.LineDTO;
import mit.iwrcore.IWRCore.security.dto.LineStructureDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LineStructureService {
    private final LineStructureRepository lineStructureRepository;
    private final LineService lineService;
    private final ProplanService proplanService;

    // 저장, 삭제
    public void saveLineStructure(LineStructureDTO dto) {
        LineStructure entity=dtoToEntity(dto);
        lineStructureRepository.save(entity);
    }
    public void deleteStructure(Long id) {
        lineStructureRepository.deleteById(id);
    }

    // 변환
    public LineStructure dtoToEntity(LineStructureDTO dto){
        LineStructure entity=LineStructure.builder()
                .lsNo(dto.getLsNo())
                .line(lineService.dtoToEntity(dto.getLineDTO()))
                .proPlan(proplanService.dtoToEntity(dto.getProplanDTO()))
                .build();
        return entity;
    }
    public LineStructureDTO entityToDto(LineStructure entity){
        LineStructureDTO dto=LineStructureDTO.builder()
                .lsNo(entity.getLsNo())
                .lineDTO(lineService.entityToDto(entity.getLine()))
                .proplanDTO(proplanService.entityToDTO(entity.getProPlan()))
                .build();
        return dto;
    }

    // 조회
    public LineStructureDTO getLineStructure(Long id){
        LineStructure entity=lineStructureRepository.findById(id).get();
        return entityToDto(entity);
    };
    public List<LineStructureDTO> getLineStructureByProPlanNo(Long proplanNo) {
        List<LineStructure> list=lineStructureRepository.findByProPlanNo(proplanNo);
        return list.stream().map(x->entityToDto(x)).toList();
    }
}
