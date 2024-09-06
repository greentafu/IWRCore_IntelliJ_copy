package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.Partner;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface PartnerRepositoryCustom {
    Page<Partner> findPartnerByCustomQuery(PageRequestDTO requestDTO);
}
