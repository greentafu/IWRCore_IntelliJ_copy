package mit.iwrcore.IWRCore.service;


import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.Plan;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.repository.PlanRepository;
import mit.iwrcore.IWRCore.repository.ProductRepository;
import mit.iwrcore.IWRCore.security.dto.PlanDTO;
import mit.iwrcore.IWRCore.security.service.PlanService;
import mit.iwrcore.IWRCore.security.service.PlanServiceImpl;
import mit.iwrcore.IWRCore.security.service.ProductService;
import mit.iwrcore.IWRCore.security.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class planServiceTests {
    @Autowired
    private PlanService planService;
    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    @Commit
    public void insert(){

    }

}