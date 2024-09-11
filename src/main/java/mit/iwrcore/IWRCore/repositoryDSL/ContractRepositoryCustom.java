package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import org.springframework.data.domain.Page;

public interface ContractRepositoryCustom {
    Page<Object[]> findContractByCustomQuery(PageRequestDTO2 requestDTO);
}
