package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.Contract;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import org.springframework.data.domain.Page;

public interface ContractRepositoryCustom {
    // 조달계획 수립목록
    Page<Object[]> findContractByCustomQuery(PageRequestDTO2 requestDTO);
    // 계약등록필요 목록
    Page<Object[]> findContractByCustomQuery2(PageRequestDTO requestDTO);
    // 협력회사 계약목록
    Page<Contract> partnerContractPage(PageRequestDTO requestDTO);
    // 재고> 재고 목록
    Page<Object[]> stockQuantityPage(PageRequestDTO requestDTO);
    // 재고> 재고 상세목록
    Page<Object[]> stockDetailPage(PageRequestDTO requestDTO);
}
