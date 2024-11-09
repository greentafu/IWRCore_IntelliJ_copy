package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.FileProduct;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.repository.ProductRepository;
import mit.iwrcore.IWRCore.security.dto.ProplanDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProCodeService proCodeService;
    private final MemberService memberService;

    // 저장, 삭제
    @Override
    public ProductDTO saveProduct(ProductDTO productDTO, List<FileProduct> fileList) {
        Product product = dtoToEntity(productDTO);
        product.setFiles(fileList);
        Product savedProduct=productRepository.save(product);
        return entityToDto(savedProduct);
    }
    @Override
    public void deleteProduct(Long productID) {
        productRepository.deleteById(productID);
    }

    // 변환
    @Override
    public Product dtoToEntity(ProductDTO dto) {
        Product product=Product.builder()
                .manuCode(dto.getManuCode())
                .name(dto.getName())
                .color(dto.getColor())
                .text(dto.getText())
                .uuid(dto.getUuid())
                .supervisor(dto.getSupervisor())
                .mater_imsi(dto.getMater_imsi())
                .mater_check(dto.getMater_check())
                .proS(proCodeService.proSdtoToEntity(dto.getProSDTO()))
                .member(memberService.memberdtoToEntity(dto.getMemberDTO()))
                .build();
        return product;
    }
    @Override
    public ProductDTO entityToDto(Product entity) {
        ProductDTO productDTO = ProductDTO.builder()
                .manuCode(entity.getManuCode())
                .name(entity.getName())
                .color(entity.getColor())
                .text(entity.getText())
                .uuid(entity.getUuid())
                .supervisor(entity.getSupervisor())
                .mater_imsi(entity.getMater_imsi())
                .mater_check(entity.getMater_check())
                .regDate(entity.getRegDate())
                .proSDTO(proCodeService.proSTodto(entity.getProS()))
                .memberDTO(memberService.memberTodto(entity.getMember()))
                .build();
        return productDTO;
    }

    // 조회
    @Override
    public ProductDTO getProduct(Long productID) {
        return entityToDto(productRepository.findProduct(productID));
    }
    @Override
    public Long newProductCount(){
        Long count=productRepository.newProductCount();
        return (count!=null)?count:0L;
    }


    // 생산부서> 모든 제품 목록
    @Override
    public PageResultDTO<ProductDTO, Product> getAllProducts(PageRequestDTO requestDTO) {
        Page<Product> entityPage = productRepository.findProductByCustomQuery(requestDTO);
        Function<Product, ProductDTO> fn=(entity->entityToDto(entity));
        return new PageResultDTO<>(entityPage, fn);
    }
    // 생산부서> 생산계획이 없는 제품 목록
    @Override
    public PageResultDTO<ProductDTO, Product> getNonPlanProducts(PageRequestDTO requestDTO){
        Page<Product> entityPage=productRepository.findProductByCustomQuery4(requestDTO);
        Function<Product, ProductDTO> fn=(entity->entityToDto(entity));
        return new PageResultDTO<>(entityPage, fn);
    }
    // 개발부서> 임시저장 했으나 최종확인은 하지 않은 제품 리스트
    @Override
    public PageResultDTO<ProductDTO, Product> getNonCheckProducts(PageRequestDTO requestDTO){
        Page<Product> entityPage=productRepository.findProductByCustomQuery2(requestDTO);
        Function<Product, ProductDTO> fn=(entity->entityToDto(entity));
        return new PageResultDTO<>(entityPage, fn);
    }
    // 제품관리> 최종확인까지 완료한 제품 리스트
    @Override
    public PageResultDTO<ProductDTO, Product> getCheckProducts(PageRequestDTO requestDTO) {
        Page<Product> entityPage=productRepository.findProductByCustomQuery3(requestDTO);
        Function<Product, ProductDTO> fn=(entity->entityToDto(entity));
        return new PageResultDTO<>(entityPage, fn);
    }








    @Override
    public List<ProplanDTO> convertProPlans(Product entity) {
//        if (entity == null || entity.getProPlans() == null) {
//            return Collections.emptyList(); // proPlans가 null일 경우 빈 리스트 반환
//        }
//        return entity.getProPlans().stream()
//                .map(proPlan -> ProplanDTO.builder()
//                        .proplanNo(proPlan.getProplanNo())
//                        .pronum(proPlan.getPronum())
//                        .filename(proPlan.getFilename())
//                        .startDate(proPlan.getStartDate())
//                        .endDate(proPlan.getEndDate())
//                        .line(proPlan.getLine())
//                        .details(proPlan.getDetails())
//                        .build())
//                .collect(Collectors.toList());
        return null;
    }
    @Override
    public List<ProductDTO> searchProducts(String query) {
//        List<Product> products;
//        if (query == null || query.trim().isEmpty()) {
//            products = productRepository.findAll(); // 모든 제품 가져오기
//        } else {
//            products = productRepository.searchProducts(query); // 검색 결과 가져오기
//        }
//
//        // Product 리스트를 ProductDTO 리스트로 변환
//        return products.stream()
//                .map(productEntity -> {
//                    ProductDTO productDTO = entityToDto(productEntity);
//                    // ProPlans 추가
//                    List<ProplanDTO> proPlans = convertProPlans(productEntity);
//                    productDTO.setProPlans(proPlans);
//                    return productDTO;
//                })
//                .collect(Collectors.toList());
        return null;
    }
}


