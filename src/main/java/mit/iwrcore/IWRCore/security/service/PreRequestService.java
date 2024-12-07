package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.PreRequest;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.PreRequestDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.PreRequestCountDTO;
public interface PreRequestService {
    // 저장, 삭제
    PreRequestDTO savePreRequest(PreRequestDTO preRequestDTO);
    void deletePreRequest(Long preCode);
    void updateAllCheck(Long preReqCode);

    // 변환
    PreRequest dtoToEntity(PreRequestDTO dto);
    PreRequestDTO entityToDTO(PreRequest entity);

    // 조회
    PreRequestDTO getPreRequest(Long preCode);


    // 출하요청> 출하목록
    PageResultDTO<PreRequestCountDTO, Object[]> requestPage(PageRequestDTO requestDTO);
}
