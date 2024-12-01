package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.Returns;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface ReturnsRepositoryCustom {
    // 협력회사 반품 목록
    Page<Returns> partnerReturnsPage(PageRequestDTO requestDTO);
}
