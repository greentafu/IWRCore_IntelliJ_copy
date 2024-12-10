package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Returns;
import mit.iwrcore.IWRCore.repositoryDSL.ReturnsRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReturnsRepository extends JpaRepository<Returns,Long>, ReturnsRepositoryCustom {
    @Transactional
    @EntityGraph(attributePaths = {"shipment", "writer"})
    @Query("select r from Returns r where r.shipment.balju.baljuNo=:baljuNo")
    List<Returns> getReturns(Long baljuNo);

    @Modifying
    @Transactional
    @EntityGraph(attributePaths = {"shipment", "writer"})
    @Query("update Returns r set r.returnCheck=1 where r.reNO=:reNO")
    void updateReturnsCheck(Long reNO);
}
