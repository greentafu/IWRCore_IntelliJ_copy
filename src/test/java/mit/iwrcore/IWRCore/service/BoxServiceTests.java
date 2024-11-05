package mit.iwrcore.IWRCore.service;

import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.Box;
import mit.iwrcore.IWRCore.entity.Material;
import mit.iwrcore.IWRCore.entity.Member;
import mit.iwrcore.IWRCore.repository.BoxRepository;
import mit.iwrcore.IWRCore.security.dto.BoxDTO;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.security.service.BoxServiceImpl;
import mit.iwrcore.IWRCore.security.service.MaterialService;
import mit.iwrcore.IWRCore.security.service.MemberService;
import mit.iwrcore.IWRCore.security.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

@SpringBootTest
public class BoxServiceTests {
    @Autowired
    private BoxServiceImpl boxService;

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private ProductService productService;



}
