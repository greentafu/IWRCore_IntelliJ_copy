package mit.iwrcore.IWRCore.controller_CRUD;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.Invoice;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.InvoiceTextDTO;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.SaveInvoiceDTO;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileContractDTO;
import mit.iwrcore.IWRCore.security.dto.InvoiceDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.ShipmentDTO;
import mit.iwrcore.IWRCore.security.service.InvoiceService;
import mit.iwrcore.IWRCore.security.service.MemberService;
import mit.iwrcore.IWRCore.security.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class CRUDInvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private MemberService memberService;

    @PostMapping("/saveInvoice")
    @ResponseBody
    public void saveInvoice(@RequestBody SaveInvoiceDTO saveInvoiceDTO){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO=(AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO=memberService.findMemberDto(authMemberDTO.getMno(), null);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime=LocalDateTime.parse(saveInvoiceDTO.getWriteDate()+" 00:00:00", formatter);

        InvoiceDTO invoiceDTO=InvoiceDTO.builder()
                .tranNO((saveInvoiceDTO.getTranNO()!=null)? saveInvoiceDTO.getTranNO() : null)
                .plz(saveInvoiceDTO.getRadio())
                .dateCreated(localDateTime)
                .text(saveInvoiceDTO.getText())
                .email1(saveInvoiceDTO.getEmail1())
                .email2(saveInvoiceDTO.getEmail2())
                .cash((saveInvoiceDTO.getCash()!=null)? saveInvoiceDTO.getCash() : null)
                .cheque((saveInvoiceDTO.getCheque()!=null)? saveInvoiceDTO.getCheque() : null)
                .promissory((saveInvoiceDTO.getPromissory()!=null)? saveInvoiceDTO.getPromissory() : null)
                .receivable((saveInvoiceDTO.getReceivable()!=null)? saveInvoiceDTO.getReceivable() : null)
                .memberDTO(memberDTO).build();
        InvoiceDTO savedInvoiceDTO=invoiceService.saveInvoice(invoiceDTO);
        Invoice invoice=invoiceService.dtoToEntity(savedInvoiceDTO);

        List<ShipmentDTO> shipmentDTOs=(savedInvoiceDTO.getTranNO()!=null)?shipmentService.getShipmentByInvoice(savedInvoiceDTO.getTranNO()):null;
        List<Long> shipmentNo=new ArrayList<>();
        List<Long> exShipNo=new ArrayList<>();
        if(shipmentDTOs!=null) shipmentDTOs.stream().forEach(x->shipmentNo.add(x.getShipNO()));

        List<InvoiceTextDTO> invoiceTextDTOs=saveInvoiceDTO.getInvoiceTextDTOs();
        for(InvoiceTextDTO invoiceTextDTO:invoiceTextDTOs){
            if(shipmentNo.size()==0){
                shipmentService.updateShipmentInvoice(invoice, invoiceTextDTO.getShipText(), invoiceTextDTO.getShipNo());
            }else{
                if(shipmentNo.contains(invoiceTextDTO.getShipNo())){
                    shipmentService.updateShipmentInvoice(invoice, invoiceTextDTO.getShipText(), invoiceTextDTO.getShipNo());
                    exShipNo.add(invoiceTextDTO.getShipNo());
                }
            }
        }
        for(Long temp:shipmentNo){
            if(!exShipNo.contains(temp)){
                shipmentService.updateShipmentInvoicebGo(temp);
            }
        }
    }
    @GetMapping("/deleteInvoice")
    public void deleteInvoice(@RequestParam(required = false) Long tranNO){
        List<ShipmentDTO> shipmentDTOList=shipmentService.getShipmentByInvoice(tranNO);
        shipmentDTOList.stream().forEach(x->shipmentService.updateShipmentInvoice(null, null, x.getShipNO()));
        invoiceService.deleteInvoice(tranNO);
    }
}
