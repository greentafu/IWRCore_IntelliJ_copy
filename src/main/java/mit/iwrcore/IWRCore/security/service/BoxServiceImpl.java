package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.Box;
import mit.iwrcore.IWRCore.repository.BoxRepository;
import mit.iwrcore.IWRCore.security.dto.BoxDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoxServiceImpl implements BoxService {
    private final BoxRepository boxRepository;

    // 저장, 삭제

    // 변환
    @Override
    public Box dtoToEntity(BoxDTO dto){
        Box box=null;
        if(dto!=null) box= Box.builder()
                .boxCode(dto.getBoxcode())
                .boxName(dto.getBoxname()).build();
        return box;
    }
    @Override
    public BoxDTO entityToDto(Box entity){
        BoxDTO boxDTO=null;
        if(entity!=null) boxDTO=BoxDTO.builder()
                .boxcode(entity.getBoxCode())
                .boxname(entity.getBoxName()).build();
        return boxDTO;
    }

    // 조회
    @Override
    public BoxDTO getBox(Long boxId){
        return entityToDto(boxRepository.getReferenceById(boxId));
    }
    @Override
    public List<BoxDTO> getAllBoxlist() {
        List<Box> boxes = boxRepository.findAll();
        return boxes.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}