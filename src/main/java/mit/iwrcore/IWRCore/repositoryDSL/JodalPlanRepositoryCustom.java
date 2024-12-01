package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.JodalPlan;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JodalPlanRepositoryCustom {
    Page<JodalPlan> findJodalPlanByCustomQuery(PageRequestDTO requestDTO);

    // 출하요청> 생산계획에 따른 제품 구성 및 재고
    List<Object[]> getStructureStock(Long proplanNo);
    // 계약서> 계약하지 않은 조달계획 목록
    Page<Object[]> noneContractJodalPlan(PageRequestDTO2 requestDTO2);
}
