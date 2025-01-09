package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.FileProduct;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    // 저장, 삭제
    ProductDTO saveProduct(ProductDTO productDTO, List<FileProduct> fileList);
    void deleteProduct(Long productId);

    // 변환
    Product dtoToEntity(ProductDTO dto);
    ProductDTO entityToDto(Product entity);

    // 조회
    ProductDTO getProduct(Long productID);
    Long newProductCount();
    List<ProductDTO> getProductByCategory(Long type, Long code);


    // 생산부서> 모든 제품 목록
    PageResultDTO<ProductDTO, Product> getAllProducts(PageRequestDTO requestDTO);
    // 생산부서> 생산계획이 없는 제품 목록
    PageResultDTO<ProductDTO, Product> getNonPlanProducts(PageRequestDTO requestDTO);
    // 개발부서> 임시저장 했으나 최종확인은 하지 않은 제품 리스트
    PageResultDTO<ProductDTO, Product> getNonCheckProducts(PageRequestDTO requestDTO);
    // 제품관리> 최종확인까지 완료한 제품 리스트
    PageResultDTO<ProductDTO, Product> getCheckProducts(PageRequestDTO requestDTO);
}
