package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthPartnerDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.PartnerMainDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.QuantityDateDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/partner")
@RequiredArgsConstructor
@Log4j2
public class PartnerController {
    private final EmergencyService emergencyService;
    private final ContractService contractService;
    private final BaljuService baljuService;
    private final ReturnsService returnsService;
    private final ShipmentService shipmentService;

    @GetMapping("/main")
    public void main(Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthPartnerDTO authPartnerDTO=(AuthPartnerDTO) authentication.getPrincipal();
        model.addAttribute("baljuDTOs", baljuService.partListBalju(authPartnerDTO.getPno()));
    }
    @GetMapping("/view_product")
    public void view_product(@RequestParam Long baljuNo, Model model){
        model.addAttribute("baljuDTO", baljuService.getBalju(baljuNo));
    }
    @GetMapping("/view_return")
    public void view_return(@RequestParam Long reNO, Model model){
        model.addAttribute("returnsDTO", returnsService.getReturns(reNO));
    }
    @GetMapping("/list_contract")
    public void list_contract(PageRequestDTO requestDTO, Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthPartnerDTO authPartnerDTO=(AuthPartnerDTO) authentication.getPrincipal();
        requestDTO.setPno(authPartnerDTO.getPno());
        model.addAttribute("contract_list", contractService.partnerContractList(requestDTO));
    }
    @GetMapping("/list_order")
    public void liat_order(PageRequestDTO requestDTO, Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthPartnerDTO authPartnerDTO=(AuthPartnerDTO) authentication.getPrincipal();
        requestDTO.setPno(authPartnerDTO.getPno());
        model.addAttribute("balju_list", baljuService.partnerBaljuList(requestDTO));
    }
    @GetMapping("/list_invoice")
    public void list_invoice(PageRequestDTO requestDTO, Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthPartnerDTO authPartnerDTO=(AuthPartnerDTO) authentication.getPrincipal();
        requestDTO.setPno(authPartnerDTO.getPno());
        model.addAttribute("invoice_list", shipmentService.partnerInvoicePage(requestDTO));
    }
    @GetMapping("/list_return")
    public void list_return(PageRequestDTO requestDTO, Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthPartnerDTO authPartnerDTO=(AuthPartnerDTO) authentication.getPrincipal();
        model.addAttribute("list", returnsService.getReturnPage(requestDTO, authPartnerDTO.getPno()));
    }
    @GetMapping("/list_urgent")
    public void list_urgetn(PageRequestDTO requestDTO, Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthPartnerDTO authPartnerDTO=(AuthPartnerDTO) authentication.getPrincipal();
        requestDTO.setPno(authPartnerDTO.getPno());
        model.addAttribute("urget_list", emergencyService.getAllEmergencies(requestDTO));
    }
}
