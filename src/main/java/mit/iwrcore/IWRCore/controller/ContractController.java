package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.MiniContractDTO;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.SaveContractDTO;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.JodalPlanDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PartnerDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        model.addAttribute("contract", contractService.getContractById(conNo));
    }
    @GetMapping("/contract")
    public void download_contract(@RequestParam(required = false) Long conNo, Model model){
        model.addAttribute("con", contractService.getContractById(conNo));
    }
}


