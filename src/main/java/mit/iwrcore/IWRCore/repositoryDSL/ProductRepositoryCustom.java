package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface ProductRepositoryCustom {
    Page<Product> findProductByCustomQuery(PageRequestDTO requestDTO);
}
