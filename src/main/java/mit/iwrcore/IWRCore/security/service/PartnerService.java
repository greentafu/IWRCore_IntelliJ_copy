package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Partner;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.PartnerDTO;

import java.util.List;

public interface PartnerService {

    // 협력회사 검색
    Partner findPartnerEntity(Long pno, String id, String reg_number);
    PartnerDTO findPartnerDto(Long pno, String id, String reg_number);

    // 협력회사관리> 회사 모두 보기
    PageResultDTO<PartnerDTO, Partner> findPartnerList(PageRequestDTO pageRequestDTO);
    // 계약서> 소속회사 외 협력회사 모두 보기
    PageResultDTO<PartnerDTO, Partner> getAllPartner(PageRequestDTO pageRequestDTO);
    // 발주서> 소속회사 외 발주가능한 협력회사 모두 보기
    PageResultDTO<PartnerDTO, Partner> getBaljuPartner(PageRequestDTO pageRequestDTO);

    // 회사 추가, 삭제
    Integer insertPartner(PartnerDTO dto);
    void deletePartner(Long pno);

    // entity-dto 변환
    PartnerDTO partnerTodto(Partner entity);
    Partner partnerDtoToEntity(PartnerDTO dto);

    List<PartnerDTO> partnerList();
}
