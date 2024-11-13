package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.Invoice;
import mit.iwrcore.IWRCore.entity.Shipment;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.InvoiceTextDTO;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.SaveInvoiceDTO;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.InvoiceDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.ShipmentDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final ShipmentService shipmentService;
    private final PartnerService partnerService;
    private final InvoiceService invoiceService;

    @GetMapping("/list_invoice")
    public void list_invoice(){}
    @GetMapping("/add_invoice")
    public void add_invoice(@RequestParam(required = false) Long shipNO, Model model){
        // 회사 정보 전달(고정)
        model.addAttribute("company", partnerService.findPartnerDto(1L, null, null));
        if(shipNO!=null){
            Shipment shipment=shipmentService.getShipmentEntity(shipNO);
            Long pno=shipment.getBalju().getContract().getPartner().getPno();
            model.addAttribute("pno", pno);
            model.addAttribute("shipNo", shipNO);
        }
    }
    @GetMapping("/modify_invoice")
    public void modify_invoice(Long tranNO, Model model){
        // 회사 정보 전달(고정)
        model.addAttribute("company", partnerService.findPartnerDto(1L, null, null));

        InvoiceDTO invoiceDTO=invoiceService.getInvoiceById(tranNO);
        List<ShipmentDTO> shipmentDTOs=shipmentService.getShipmentByInvoice(tranNO);

        String date=invoiceDTO.getDateCreated().toString().split("T")[0];

        model.addAttribute("invoice", invoiceDTO);
        model.addAttribute("date", date);
        model.addAttribute("partner", shipmentDTOs.get(0).getBaljuDTO().getContractDTO().getPartnerDTO());
    }
    @GetMapping("/invoice")
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
}
