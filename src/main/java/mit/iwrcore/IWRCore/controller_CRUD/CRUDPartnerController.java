package mit.iwrcore.IWRCore.controller_CRUD;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.BaljuDTO;
import mit.iwrcore.IWRCore.security.dto.GumsuDTO;
import mit.iwrcore.IWRCore.security.dto.ShipmentDTO;
import mit.iwrcore.IWRCore.security.service.BaljuService;
import mit.iwrcore.IWRCore.security.service.GumsuService;
import mit.iwrcore.IWRCore.security.service.ReturnsService;
import mit.iwrcore.IWRCore.security.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class CRUDPartnerController {
    @Autowired
    private BaljuService baljuService;
    @Autowired
    private GumsuService gumsuService;
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private ReturnsService returnsService;

    @PostMapping("/saveMakeQuantity")
    public void saveMakeQuantity(@RequestParam(required = false) Long baljuNo,
                                 @RequestParam(required = false) Long quantity){
        GumsuDTO gumsuDTO=gumsuService.getGumsuByBalju(baljuNo);
        Long allQuantity=gumsuDTO.getMake()+quantity;
        gumsuDTO.setMake(allQuantity);
        gumsuService.modifyGumsu(gumsuDTO);
    }
    @PostMapping("/saveShipment")
    public void saveShipment(@RequestParam(required = false) Long baljuNo,
                             @RequestParam(required = false) Long shipNum,
                             @RequestParam(required = false) String shipText){
        BaljuDTO baljuDTO=baljuService.getBalju(baljuNo);
        ShipmentDTO shipmentDTO=ShipmentDTO.builder()
                .shipNum(shipNum)
                .text(shipText)
                .receiveCheck(0L)
                .baljuDTO(baljuDTO).build();
        shipmentService.saveShipment(shipmentDTO);
    }
    @PostMapping("/saveReturnCheck")
    public void returnCheck(@RequestParam Long reNo){
        returnsService.modifyReturnCheck(reNo);
    }
}
