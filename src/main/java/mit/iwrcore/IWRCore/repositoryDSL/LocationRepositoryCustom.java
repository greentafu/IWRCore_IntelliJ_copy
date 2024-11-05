package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.Location;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import org.springframework.data.domain.Page;

public interface LocationRepositoryCustom {
    // 발주서> 배송지 목록
    Page<Location> findLocationByCustomQuery(PageRequestDTO2 requestDTO);
}
