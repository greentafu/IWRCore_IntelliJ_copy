package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final BaljuService baljuService;

    @GetMapping("/list_contract")
    public void list_contract(){}
    @GetMapping("/new_contract")
    public void new_contract(@RequestParam(required = false) Long joNo, Model model){
        model.addAttribute("joNo", joNo);
    }
    @GetMapping("/modify_contract")
    public void modify_contract(Long conNo, Model model){
        model.addAttribute("contract", contractService.getContract(conNo));
    }
    @GetMapping("/contract")
    public void download_contract(@RequestParam(required = false) Long conNo, Model model){
        model.addAttribute("con", contractService.getContract(conNo));
    }
}


