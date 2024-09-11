package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface GumsuRepositoryCustom {
    Page<Object[]> findGumsuByCustomQuery(PageRequestDTO requestDTO);
}
