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

    @Query("select b from Balju b where b.contract.partner.pno=:pno")
    Page<Balju> partnerBaljuList(Pageable pageable, Long pno);

    @Transactional
    @EntityGraph(attributePaths = {"contract"})
    @Query("select distinct b.contract.jodalPlan.proPlan.product from Balju b " +
            "left join Contract c on (b.contract.conNo=c.conNo) " +
            "where b.finCheck=0")
    List<Product> baljuProductList();
}