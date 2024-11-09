package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.FileReturns;
import mit.iwrcore.IWRCore.entity.Returns;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.ReturnsDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ReturnBaljuDTO;

import java.util.List;

public interface ReturnsService {
    // 저장, 삭제
    ReturnsDTO saveReturns(ReturnsDTO returnsDTO, List<FileReturns> fileList);
    void deleteReturns(Long id);

    void modifyReturnCheck(Long reNO);

    // 변환
    Returns dtoToEntity(ReturnsDTO dto);
    ReturnsDTO entityToDTO(Returns entity);

    // 조회
    ReturnsDTO getReturns(Long id);


    // 협력회사> 반품 목록 확인
    List<ReturnsDTO> getReturnsList(Long baljuNo);
    // 협력회사> 반품 목록
    PageResultDTO<ReturnBaljuDTO, Object[]> getReturnPage(PageRequestDTO requestDTO, Long pno);
}