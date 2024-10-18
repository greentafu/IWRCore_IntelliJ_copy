package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/development")
@RequiredArgsConstructor
@Log4j2
public class DevelopmentController {

    private final ProductService productService;

    @GetMapping("/input_dev")
    public void input_dev(){

    }
    @GetMapping("/list_dev")
    public void list_dev(){

    }
    @GetMapping("/detail_dev")
    public void detail_dev(){

    }
    @GetMapping("/modify_dev")
    public void modify_dev(@RequestParam Long manuCode, Model model){
        model.addAttribute("product", productService.getProductById(manuCode));
    }
}
