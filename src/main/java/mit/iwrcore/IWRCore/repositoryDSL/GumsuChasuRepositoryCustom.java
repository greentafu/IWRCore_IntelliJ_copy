package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface GumsuChasuRepositoryCustom {
    Page<Object[]> findGumsuChasuByCustomQuery(PageRequestDTO requestDTO);
}
