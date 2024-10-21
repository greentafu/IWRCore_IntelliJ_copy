package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;

@RequestMapping("/material")
@RequiredArgsConstructor
@Log4j2
@Controller
public class MaterialController {
    private final MaterialService materialService;
    private final BoxService boxService;

    @GetMapping("/list_material")
    public void list_material(Model model) {
        model.addAttribute("box_list", boxService.list());
    }
    @GetMapping("/new_material")
    public void new_material(Model model) {
        model.addAttribute("boxList", boxService.list());
    }
    @GetMapping("/modify_material")
    public void modify_material(@RequestParam Long materCode, Model model) {
        model.addAttribute("material", materialService.findM(materCode));
        model.addAttribute("boxList", boxService.list());
    }
    @GetMapping("/material")
    public void material(@RequestParam Long materCode, Model model) {
        model.addAttribute("material", materialService.findM(materCode));
    }
}



