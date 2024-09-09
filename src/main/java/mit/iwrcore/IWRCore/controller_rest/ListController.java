package mit.iwrcore.IWRCore.controller_rest;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.Material;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.service.MaterialService;
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

    @GetMapping("/materialList")
    public PageResultDTO<MaterialDTO, Material> materialList(@RequestParam(required = false) int page,
                                                             @RequestParam(required = false) Long selectProL,
                                                             @RequestParam(required = false) Long selectProM,
                                                             @RequestParam(required = false) Long selectProS,
                                                             @RequestParam(required = false) String productSearch,
                                                             @RequestParam(required = false) Long selectedBox,
                                                             @RequestParam(required = false) Long selectMaterL,
                                                             @RequestParam(required = false) Long selectMaterM,
                                                             @RequestParam(required = false) Long selectMaterS,
                                                             @RequestParam(required = false) String materialSearch){

        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }
        if (materialSearch != null && materialSearch.trim().isEmpty()) { materialSearch = null; }

        System.out.println("@@@@@@@@@@@@@@@"+page+"/"+selectProL+"/"+selectProM+"/"+selectProS+"/"+productSearch+"/"+selectedBox+"/"+selectMaterL+"/"+selectMaterM+"/"+selectMaterS+"/"+materialSearch);

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(10)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .materL(selectMaterL).materM(selectMaterM).materS(selectMaterS).materialSearch(materialSearch).box(selectedBox).build();

        return materialService.findMaterialAll(requestDTO);
    }
}
