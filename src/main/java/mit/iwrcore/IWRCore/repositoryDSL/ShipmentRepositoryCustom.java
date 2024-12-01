package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.Shipment;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import org.springframework.data.domain.Page;

public interface ShipmentRepositoryCustom {
    // 입고반품 목록
    Page<Object[]> findShipmentByCustomQuery(PageRequestDTO requestDTO);
    // 메인화면 수령 목록
    Page<Object[]> findShipmentByCustomQuery2(PageRequestDTO requestDTO);
    // 거래명세서 전 목록
    Page<Object[]> findShipmentByCustomQuery3(PageRequestDTO requestDTO);
    // 거래명세서 목록
    Page<Object[]> findShipmentByCustomQuery4(PageRequestDTO2 requestDTO);

    // 거래명세서> 협력회사별 거래명세서 발급 가능 배송 목록
    Page<Shipment> getInvoiceShipment(PageRequestDTO2 requestDTO);
    // 협력회사 거래명세서 목록
    Page<Object[]> partnerInvoicePage(PageRequestDTO requestDTO);
}
