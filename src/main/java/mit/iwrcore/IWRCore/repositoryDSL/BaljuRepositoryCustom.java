package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import org.springframework.data.domain.Page;

public interface BaljuRepositoryCustom {
    // 계약서 목록
    Page<Object[]> findBaljuByCustomQuery(PageRequestDTO2 requestDTO);
    // 발주전 목록
    Page<Object[]> findBaljuByCustomQuery2(PageRequestDTO requestDTO);
    // 발주완료 목록
    Page<Object[]> findBaljuByCustomQuery3(PageRequestDTO2 requestDTO);
}
