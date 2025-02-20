package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.ProPlan;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import org.springframework.data.domain.Page;

public interface ProPlanRepositoryCustom {
    Page<Object[]> findProPlanByCustomQuery(PageRequestDTO2 requestDTO);

    // 출하요청> 생산계획
    Page<ProPlan> getPlanProPlan(PageRequestDTO requestDTO);
}
