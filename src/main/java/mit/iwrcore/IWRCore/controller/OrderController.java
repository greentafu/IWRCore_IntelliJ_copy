package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@Log4j2
public class OrderController {
    private final BaljuService baljuService;
    private final ContractService contractService;
    private final MemberService memberService;
    private final EmergencyService emergencyService;
    private final BaljuChasuService baljuChasuService;

    @GetMapping("/list_order")
    public void list_order(){}
    @GetMapping("/new_order")
    public void new_order2(@RequestParam(required = false) Long conNo, Model model){
        Long pno=0L;
        String name="";
        if(conNo!=null){
            PartnerDTO partnerDTO=contractService.getContract(conNo).getPartnerDTO();
            pno=partnerDTO.getPno();
            name=partnerDTO.getName();
        }
        model.addAttribute("pno", pno);
        model.addAttribute("name", name);
    }
    @GetMapping("/modify_order")
    public void modify_order2(Long baljuNo, Model model){
        PartnerDTO partnerDTO=baljuService.getBalju(baljuNo).getContractDTO().getPartnerDTO();
        model.addAttribute("partner", partnerDTO);
        model.addAttribute("baljuList", baljuChasuService.modifyBalju(partnerDTO.getPno()));
    }
    @GetMapping("/order")
    public void download_order(Long baljuNo, Model model){
        PartnerDTO partnerDTO=baljuService.getBalju(baljuNo).getContractDTO().getPartnerDTO();
        model.addAttribute("partner", partnerDTO);
        model.addAttribute("baljuList", baljuChasuService.modifyBalju(partnerDTO.getPno()));
    }
}
