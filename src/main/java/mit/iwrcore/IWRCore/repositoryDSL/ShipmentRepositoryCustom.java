package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface ShipmentRepositoryCustom {
    // 입고반품 목록
    Page<Object[]> findShipmentByCustomQuery(PageRequestDTO requestDTO);
    // 메인화면 수령 목록
    Page<Object[]> findShipmentByCustomQuery2(PageRequestDTO requestDTO);
    // 거래명세서 전 목록
    Page<Object[]> findShipmentByCustomQuery3(PageRequestDTO requestDTO);
}
