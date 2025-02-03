package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Invoice;
import mit.iwrcore.IWRCore.entity.Member;
import mit.iwrcore.IWRCore.entity.Shipment;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.ShipmentDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.InvoiceContractDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.InvoicePartnerDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ShipmentGumsuDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ShipmentReturnDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ShipmentService {
    // 저장, 삭제
    ShipmentDTO saveShipment(ShipmentDTO shipmentDTO);

    void updateShipmentInvoicebGo(Long shipNo);
    void updateShipmentDate(LocalDateTime dateTime, Long shipNo);
    void updateMemberCheck(Member member, Long shipNo);
    void updateShipmentInvoice(Invoice invoice, String text, Long shipNo);

    // 변환
    Shipment dtoToEntity(ShipmentDTO dto);
    ShipmentDTO entityToDto(Shipment entity);

    // 조회
    ShipmentDTO getShipment(Long id);
    Shipment getShipmentEntity(Long shipNo);
    List<ShipmentDTO> getShipmentByBalju(Long baljuNo);
    List<ShipmentDTO> getShipmentByInvoice(Long tranNO);
    Long getSavedShipNum(Long baljuNo);

    ShipmentReturnDTO findShipment(Long shipNo);


    // 메인페이지> 배송갯수
    Long mainShipNum();
    // 메인페이지> 수령가능 목록
    PageResultDTO<ShipmentGumsuDTO, Object[]> mainShipment(PageRequestDTO requestDTO);
    // 입고/반품> 배송 목록
    PageResultDTO<ShipmentGumsuDTO, Object[]> pageShipment(PageRequestDTO requestDTO);
    // 거래명세서> 거래명세서 발급 필요 목록
    PageResultDTO<ShipmentDTO, Object[]> noneInvoiceShipment(PageRequestDTO requestDTO);
    // 거래명세서> 거래명세서 발급 완료 목록
    PageResultDTO<InvoicePartnerDTO, Object[]> pageFinInvoice(PageRequestDTO2 requestDTO2);
    // 거래명세서> 거래명세서 발급 가능 배송 목록
    PageResultDTO<ShipmentDTO, Shipment> couldInvoiceShipment(PageRequestDTO2 requestDTO2);
    // 협력회사> 거래명세서 목록
    PageResultDTO<InvoiceContractDTO, Object[]> partnerInvoicePage(PageRequestDTO requestDTO);



    List<ShipmentDTO> getShipmentsByReceiveCheck(long receiveCheck);
}