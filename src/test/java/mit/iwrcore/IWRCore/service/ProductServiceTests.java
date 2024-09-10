package mit.iwrcore.IWRCore.service;

import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.repository.ProductRepository;
import mit.iwrcore.IWRCore.security.service.MemberService;
import mit.iwrcore.IWRCore.security.service.ProCodeService;
import mit.iwrcore.IWRCore.security.service.ProductService;
import mit.iwrcore.IWRCore.security.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductServiceImpl productServiceImpl;
    @Autowired
    private ProCodeService proCodeService;
    @Autowired
    private MemberService memberService;
    // 데이터 삽입
    @Test
    @Transactional
    @Commit
    public void insert(){
        ProductDTO dto=ProductDTO.builder()
                .name("A자전거").color("빨강").text("").uuid("")
                .supervisor("감독자1").mater_imsi(0L).mater_check(0L)
                .memberDTO(memberService.findMemberDto(1L, null))
                .proSDTO(proCodeService.findProS(1L))
                .build();
        productRepository.save(productServiceImpl.productDtoToEntity(dto));
    }
    @Test
    @Transactional
    @Commit
    public void test23(){
        Pageable pageable=PageRequest.of(0,2);
    }

    @Test
    public void test(){
        PageRequestDTO requestDTO=PageRequestDTO.builder().page(1).size(2).build();
        System.out.println(productRepository.findProductByCustomQuery(requestDTO));
    }

}