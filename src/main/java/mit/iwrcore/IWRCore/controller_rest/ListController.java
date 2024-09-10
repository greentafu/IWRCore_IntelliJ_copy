package mit.iwrcore.IWRCore.controller_rest;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.Material;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.security.service.MaterialService;
import mit.iwrcore.IWRCore.security.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/list")
@Log4j2
public class ListController {
    @Autowired
    private MaterialService materialService;
    @Autowired
    private ProductService productService;

    @GetMapping("/materialList")
    public PageResultDTO<MaterialDTO, Material> materialList(@RequestParam(required = false) int page,
                                                             @RequestParam(required = false) Long selectProL,@RequestParam(required = false) Long selectProM,
                                                             @RequestParam(required = false) Long selectProS,@RequestParam(required = false) String productSearch,
                                                             @RequestParam(required = false) Long selectedBox,
                                                             @RequestParam(required = false) Long selectMaterL,@RequestParam(required = false) Long selectMaterM,
                                                             @RequestParam(required = false) Long selectMaterS,@RequestParam(required = false) String materialSearch){

        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }
        if (materialSearch != null && materialSearch.trim().isEmpty()) { materialSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .materL(selectMaterL).materM(selectMaterM).materS(selectMaterS).materialSearch(materialSearch).box(selectedBox).build();

        return materialService.findMaterialAll(requestDTO);
    }

    @GetMapping("/productDevList")
    public PageResultDTO<ProductDTO, Product> productDevList(@RequestParam(required = false) int page,
                                                             @RequestParam(required = false) Long selectProL,@RequestParam(required = false) Long selectProM,
                                                             @RequestParam(required = false) Long selectProS,@RequestParam(required = false) String productSearch,
                                                             @RequestParam(required = false) Long productImsiCheck){
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .productImsiCheck(productImsiCheck).build();

        return productService.getAllProducts(requestDTO);
    }

    @GetMapping("/nonCheckProductList")
    public PageResultDTO<ProductDTO, Product> nonCheckProductList(@RequestParam(required = false) int page,
                                                                  @RequestParam(required = false) Long selectProL,@RequestParam(required = false) Long selectProM,
                                                                  @RequestParam(required = false) Long selectProS,@RequestParam(required = false) String productSearch){
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch).build();

        return productService.getNonCheckProducts(requestDTO);
    }
}
