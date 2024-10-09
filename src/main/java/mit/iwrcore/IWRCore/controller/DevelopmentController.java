package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.Structure;
import mit.iwrcore.IWRCore.repository.MaterialRepository;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.MaterQuantityDTO;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.SaveProductDTO;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.ProDTO.ProCodeListDTO;
import mit.iwrcore.IWRCore.security.dto.ProDTO.ProSDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.security.dto.StructureDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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
