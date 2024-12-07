package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/goodshandling")
@RequiredArgsConstructor
public class GoodsHandlingController {
    private final ShipmentService shipmentService;
    private final PreRequestService preRequestService;

    @GetMapping("/list_received")
    public void list_received(){}
    @GetMapping("/return_received")
    public void return_received(@RequestParam Long shipNo, Model model){
        model.addAttribute("shipment", shipmentService.findShipment(shipNo));
    }
    @GetMapping("/view_received")
    public void view_received(@RequestParam Long shipNO, Model model){
        model.addAttribute("shipment", shipmentService.entityToDto(shipmentService.getShipmentEntity(shipNO)));
    }

    @GetMapping("/list_request")
    public void list_request(){}
    @GetMapping("/view_request")
    public void view_request(@RequestParam Long preReqCode, Model model){
        model.addAttribute("preRequestDTO", preRequestService.getPreRequest(preReqCode));
    }
}
