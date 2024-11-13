package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.Partner;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface PartnerRepositoryCustom {
    Page<Partner> findPartnerByCustomQuery(PageRequestDTO requestDTO);

    // 계약서> 소속회사 외 협력회사 모두 보기
    Page<Partner> getAllPartner(PageRequestDTO requestDTO);
    // 발주서> 소속회사 외 발주가능한 협력회사 모두 보기
    Page<Partner> getBaljuPartner(PageRequestDTO requestDTO);
    // 검수차수> 소속회사 외 검수차수 설정 가능한 협력회사 모두 보기
    Page<Partner> getGumsuPartner(PageRequestDTO requestDTO);
    // 거래명세서> 소속회사 외 거래명세서 발급 가능한 협력회사 모두 보기
    Page<Partner> getInvoicePartner(PageRequestDTO requestDTO);
}
