package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.ProPlan;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProPlanRepositoryCustom {
    Page<Object[]> findProPlanByCustomQuery(PageRequestDTO2 requestDTO);

    // 출하요청> 생산계획
    Page<ProPlan> getPlanProPlan(PageRequestDTO requestDTO);
//    // 출하요청> 생산계획에 따른 제품 구성 및 재고
//    List<Object[]> getStructureStock(Long proplanNo);
}
