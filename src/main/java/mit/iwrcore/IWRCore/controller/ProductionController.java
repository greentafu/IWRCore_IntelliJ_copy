package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.security.dto.StructureDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/production")
@RequiredArgsConstructor
public class ProductionController {
    private final ProductService productService;
    private final StructureService structureService;
    private final FileService fileService;

    @GetMapping("/list_manufacture")
    public void list_manufacture() {}
    @GetMapping("/list_newProduct")
    public void list_newProduct() {}
    @GetMapping("/new_manufacture")
    public void new_manufacture() {}
    @GetMapping("/check_manufacture")
    public void check_manufacture(@RequestParam Long manuCode, Model model) {
        model.addAttribute("product", productService.getProductById(manuCode));
    }
    @GetMapping("/modify_manufacture")
    public void modify_manufacture(@RequestParam Long manuCode, Model model) {
        model.addAttribute("product", productService.getProductById(manuCode));
    }
    @GetMapping("/manufacture")
    public void manufacture(@RequestParam Long manuCode, Model model) {
        model.addAttribute("product", productService.getProductById(manuCode));
        model.addAttribute("pFiles", fileService.getProductFileList(manuCode));
        model.addAttribute("structures", structureService.findByProduct_ManuCode(manuCode));
    }
    @GetMapping("/structures")
    public List<StructureDTO> getStructures(@RequestParam Long manuCode) {
        return structureService.findByProduct_ManuCode(manuCode);
    }
}
