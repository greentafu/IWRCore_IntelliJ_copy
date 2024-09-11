package mit.iwrcore.IWRCore.controller_rest;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.Material;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanContractNumDTO;
import mit.iwrcore.IWRCore.security.service.MaterialService;
import mit.iwrcore.IWRCore.security.service.ProductService;
import mit.iwrcore.IWRCore.security.service.ProplanService;
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
    @Autowired
    private ProplanService proplanService;

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

    @GetMapping("/checkedProductList")
    public PageResultDTO<ProductDTO, Product> checkedProductList(@RequestParam(required = false) int page,
                                                             @RequestParam(required = false) Long selectProL,@RequestParam(required = false) Long selectProM,
                                                             @RequestParam(required = false) Long selectProS,@RequestParam(required = false) String productSearch){
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch).build();

        return productService.getCheckProducts(requestDTO);
    }

    @GetMapping("/nonPlanProductList")
    public PageResultDTO<ProductDTO, Product> nonPlanProductList(@RequestParam(required = false) int page,
                                                                 @RequestParam(required = false) Long selectProL,@RequestParam(required = false) Long selectProM,
                                                                 @RequestParam(required = false) Long selectProS,@RequestParam(required = false) String productSearch){
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(10)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch).build();

        return productService.getNonPlanProducts(requestDTO);
    }

    @GetMapping("/proPlanList")
    public PageResultDTO<ProPlanContractNumDTO, Object[]> proPlanList(@RequestParam(required = false) int page2,
                                                                      @RequestParam(required = false) Long selectProL2,@RequestParam(required = false) Long selectProM2,
                                                                      @RequestParam(required = false) Long selectProS2,@RequestParam(required = false) String productSearch2,
                                                                      @RequestParam(required = false) Long proplanProgress2){
        if (productSearch2 != null && productSearch2.trim().isEmpty()) { productSearch2 = null; }

        PageRequestDTO2 requestDTO=PageRequestDTO2.builder()
                .page2(page2).size2(15)
                .proL2(selectProL2).proM2(selectProM2).proS2(selectProS2).productSearch2(productSearch2)
                .proplanProgress2(proplanProgress2).build();

        return proplanService.proplanList2(requestDTO);
    }
}
