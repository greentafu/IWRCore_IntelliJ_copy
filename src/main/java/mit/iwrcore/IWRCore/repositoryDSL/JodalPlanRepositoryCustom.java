package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.JodalPlan;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface JodalPlanRepositoryCustom {
    Page<JodalPlan> findJodalPlanByCustomQuery(PageRequestDTO requestDTO);
}
