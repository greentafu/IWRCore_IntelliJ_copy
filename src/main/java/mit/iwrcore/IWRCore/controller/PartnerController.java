package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthPartnerDTO;
import mit.iwrcore.IWRCore.security.dto.InvoiceDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PartnerDTO;
import mit.iwrcore.IWRCore.security.dto.ShipmentDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final BaljuChasuService baljuChasuService;
    private final PartnerService partnerService;
    private final InvoiceService invoiceService;

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
    // 목록 및 확인
    @GetMapping("/list_contract")
    public void list_contract(){}
    @GetMapping("/view_contract")
    public void view_contract(@RequestParam(required = false) Long conNo, Model model){
        model.addAttribute("con", contractService.getContract(conNo));
    }
    @GetMapping("/list_order")
    public void liat_order(){}
    @GetMapping("/view_order")
    public void view_order(Long baljuNo, Model model){
        PartnerDTO partnerDTO=baljuService.getBalju(baljuNo).getContractDTO().getPartnerDTO();
        model.addAttribute("baljuList", baljuChasuService.getOneBalju(baljuNo));
    }
    @GetMapping("/list_invoice")
    public void list_invoice(PageRequestDTO requestDTO, Model model){}
    @GetMapping("/view_invoice")
    public void view_invoice(Long tranNO, Model model){
        // 회사 정보 전달(고정)
        model.addAttribute("company", partnerService.findPartnerDto(1L, null, null));

        InvoiceDTO invoiceDTO=invoiceService.getInvoiceById(tranNO);
        List<ShipmentDTO> shipmentDTOs=shipmentService.getShipmentByInvoice(tranNO);

        String date=invoiceDTO.getDateCreated().toString().split("T")[0];

        model.addAttribute("invoice", invoiceDTO);
        model.addAttribute("date", date);
        model.addAttribute("partner", shipmentDTOs.get(0).getBaljuDTO().getContractDTO().getPartnerDTO());
        model.addAttribute("shipmentList", shipmentDTOs);
    }
    @GetMapping("/list_return")
    public void list_return(){}
    @GetMapping("/view_return")
    public void view_return(@RequestParam Long reNO, Model model){
        model.addAttribute("returnsDTO", returnsService.getReturns(reNO));
    }
    @GetMapping("/list_urgent")
    public void list_urgetn(PageRequestDTO requestDTO, Model model){}
}
