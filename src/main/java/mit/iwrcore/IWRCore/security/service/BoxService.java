package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Box;

import mit.iwrcore.IWRCore.security.dto.BoxDTO;

import java.util.List;

public interface BoxService {
    // 저장, 삭제

    // 변환
    Box dtoToEntity(BoxDTO dto);
    BoxDTO entityToDto(Box entity);

    // 조회
    BoxDTO getBox(Long boxId);
    List<BoxDTO> getAllBoxlist();
}
