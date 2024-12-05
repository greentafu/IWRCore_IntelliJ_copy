package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.security.service.BoxService;
import mit.iwrcore.IWRCore.security.service.ContractService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jago")
@RequiredArgsConstructor
public class JagoController {
    private final ContractService contractService;
    private final BoxService boxService;

    @GetMapping("/list_stock")
    public void list_stock(Model model){
        model.addAttribute("box_list", boxService.getAllBoxlist());
    }
    @GetMapping("/list_stockM")
    public void list_stockM(Model model){}
    @GetMapping("/stock")
    public void stock(Long materCode, Model model){
        model.addAttribute("materCode", materCode);
        model.addAttribute("yearList", contractService.getConDateList());
    }
}
