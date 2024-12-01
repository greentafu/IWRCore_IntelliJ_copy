package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.LineRepository;
import mit.iwrcore.IWRCore.security.dto.LineDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;

    public List<LineDTO> getLines(){
        List<Line> lines=lineRepository.findAll();
        return lines.stream().map(x->entityToDto(x)).toList();
    }

    public LineDTO getOneLine(Long id){
        Line line= lineRepository.findById(id).get();
        return entityToDto(line);
    }

    public LineDTO entityToDto(Line entity){
        LineDTO dto=LineDTO.builder()
                .lineCode(entity.getLineCode())
                .lineName(entity.getLineName())
                .build();
        return dto;
    }
    public Line dtoToEntity(LineDTO dto){
        Line entity=Line.builder()
                .lineCode(dto.getLineCode())
                .lineName(dto.getLineName())
                .build();
        return entity;
    }
}
