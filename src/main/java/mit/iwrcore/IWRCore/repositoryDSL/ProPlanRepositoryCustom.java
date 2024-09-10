package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface ProPlanRepositoryCustom {
    Page<Object[]> findProPlanByCustomQuery(PageRequestDTO requestDTO);
}
