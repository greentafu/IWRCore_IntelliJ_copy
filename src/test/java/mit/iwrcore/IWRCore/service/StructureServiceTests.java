package mit.iwrcore.IWRCore.service;

import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.Material;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.entity.Structure;
import mit.iwrcore.IWRCore.repository.MaterialRepository;
import mit.iwrcore.IWRCore.repository.ProductRepository;
import mit.iwrcore.IWRCore.repository.StructureRepository;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.security.dto.StructureDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

@SpringBootTest
public class StructureServiceTests {
    @Autowired
    private StructureServiceImpl structureService;
    @Autowired
    private  MaterialService materialService;
    @Autowired
    private ProductService productService;

    @Autowired
    private StructureRepository structureRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductServiceImpl productServicei;
    @Autowired
    private MaterialServiceImpl materialServiceImpl;

    private static Material material;
    private static Product product;
    private static Structure structure;


}


