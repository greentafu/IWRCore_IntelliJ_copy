package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repositoryDSL.ShipmentRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment,Long>, ShipmentRepositoryCustom {
    @Query("select s from Shipment s where s.balju.baljuNo=:baljuNo")
    List<Shipment> getShipmentByBalju(Long baljuNo);

    @Modifying
    @Transactional
    @Query("update Shipment s set s.receipt=:receiveDate where s.shipNO=:shipNo")
    void updateShipmentDate(LocalDateTime receiveDate, Long shipNo);

    @Modifying
    @Transactional
    @EntityGraph(attributePaths = {"balju"})
    @Query("update Shipment s set s.writer=:member, s.receiveCheck=1 where s.shipNO=:shipNo")
    void updateShipmentMemberCheck(Member member, Long shipNo);

    @Modifying
    @Transactional
    @EntityGraph(attributePaths = {"balju", "writer", "returns", "invoice"})
    @Query("update Shipment s set s.invoice=null, s.bGo=null where s.shipNO=:shipNo")
    void updateShipmentInvoiceText(Long shipNo);

    @Modifying
    @Transactional
    @EntityGraph(attributePaths = {"balju", "writer", "returns", "invoice"})
    @Query("update Shipment s set s.invoice=:invoice, s.bGo=:text where s.shipNO=:shipNo")
    void updateShipmentInvoice(Invoice invoice, String text, Long shipNo);

    @Transactional
    @EntityGraph(attributePaths = {"balju", "writer", "returns", "invoice"})
    @Query("select s from Shipment s where s.shipNO=:shipNo")
    Shipment findShipment(Long shipNo);

    @Query("SELECT s FROM Shipment s " +
            "JOIN FETCH s.balju b " +
            "JOIN FETCH b.contract c " +
            "JOIN FETCH c.jodalPlan jp " +
            "JOIN FETCH jp.material m " +
            "WHERE s.receiveCheck = :receiveCheck")
    List<Shipment> findByReceiveCheckWithDetails(Long receiveCheck);

    @Query("select s from Shipment s where s.invoice.tranNO=:tranNO")
    List<Shipment> getInvoiceContent(Long tranNO);

    @Transactional
    @EntityGraph(attributePaths = {"balju", "writer", "returns", "invoice"})
    @Query("select count(s) from Shipment s " +
            "left join Returns r on (s.shipNO=r.shipment.shipNO) " +
            "where s.receiveCheck=0 and r is null")
    Long mainShipment();
}
