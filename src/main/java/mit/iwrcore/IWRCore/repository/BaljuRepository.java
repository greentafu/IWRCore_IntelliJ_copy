package mit.iwrcore.IWRCore.repository;

import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.Balju;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.repositoryDSL.BaljuRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BaljuRepository extends JpaRepository<Balju, Long>, BaljuRepositoryCustom {
    @EntityGraph(attributePaths = {"contract"})
    @Query("select b from Balju b where b.contract.partner.pno=:pno")
    List<Balju> partListBalju(Long pno);

    @Transactional
    @EntityGraph(attributePaths = {"contract"})
    @Query("select distinct b.contract.jodalPlan.proPlan.product from Balju b " +
            "left join Contract c on (b.contract.conNo=c.conNo) " +
            "where b.finCheck=0")
    List<Product> baljuProductList();

    @Transactional
    @EntityGraph(attributePaths = {"contract"})
    @Query("select b from Balju b where b.contract.conNo=:conNo")
    List<Balju> baljuByContract(Long conNo);

    @Transactional
    @EntityGraph(attributePaths = {"contract"})
    @Query("select b from Balju b " +
            "where b.baljuNo=(select max(bb.baljuNo) from Balju bb where bb.contract.jodalPlan.material.materCode=:materCode)")
    Balju recentBaljuByMaterial(Long materCode);
}