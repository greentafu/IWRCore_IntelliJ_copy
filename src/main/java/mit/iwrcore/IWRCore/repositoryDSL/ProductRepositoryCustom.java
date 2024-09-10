package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface ProductRepositoryCustom {
    // 개발부서 제품목록
    Page<Product> findProductByCustomQuery(PageRequestDTO requestDTO);
    // 자재부서 최종확인 전 제품목록
    Page<Product> findProductByCustomQuery2(PageRequestDTO requestDTO);
    // 자재부서 최종확인한 제품목록
    Page<Product> findProductByCustomQuery3(PageRequestDTO requestDTO);
    // 생산부서 생산계획 없는 제품목록
    Page<Product> findProductByCustomQuery4(PageRequestDTO requestDTO);
}
