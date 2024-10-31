package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.JodalPlan;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import org.springframework.data.domain.Page;

public interface JodalPlanRepositoryCustom {
    Page<JodalPlan> findJodalPlanByCustomQuery(PageRequestDTO requestDTO);

    // 계약서> 계약하지 않은 조달계획 목록
    Page<Object[]> noneContractJodalPlan(PageRequestDTO2 requestDTO2);
}
