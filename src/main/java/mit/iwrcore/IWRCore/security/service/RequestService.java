package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Request;
import mit.iwrcore.IWRCore.security.dto.RequestDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.PreRequestSturctureDTO;

import java.util.List;

public interface RequestService {
    // 저장, 삭제
    RequestDTO saveRequest(RequestDTO requestDTO);
    void deleteRequest(Long id);
    void updateReqCheck(Long requestCode);

    // 변환
    Request dtoToEntity(RequestDTO dto);
    RequestDTO entityToDTO(Request entity);

    // 조회
    RequestDTO getRequestById(Long id);
    List<RequestDTO> getRequestByPreRequest(Long preCode);
    RequestDTO updateRequest(Long id, RequestDTO requestDTO);


    // 출하요청> 출하요청 수정시 목록
    List<PreRequestSturctureDTO> getStructureStock(Long preReqCode);
    // 메인화면> 출하갯수
    Long mainRequestCount();
}
