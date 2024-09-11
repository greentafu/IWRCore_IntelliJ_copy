package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import org.springframework.data.domain.Page;

public interface BaljuRepositoryCustom {
    Page<Object[]> findBaljuByCustomQuery(PageRequestDTO2 requestDTO);
}
