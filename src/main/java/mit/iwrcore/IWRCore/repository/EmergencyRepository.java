package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Emergency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmergencyRepository extends JpaRepository<Emergency , Long> {
    @EntityGraph(attributePaths = {"balju"})
    @Query("select e from Emergency e where e.balju.contract.partner.pno=:pno")
    Page<Emergency> findPartnerAllEmergency(Pageable pageable, Long pno);
    @EntityGraph(attributePaths = {"balju"})
    @Query("select e from Emergency e where e.balju.contract.partner.pno=:pno and e.emcheck=0")
    Page<Emergency> findPartnerNonCheckEmergency(Pageable pageable, Long pno);
    @EntityGraph(attributePaths = {"balju"})
    @Query("select e from Emergency e where e.balju.contract.partner.pno=:pno and e.emcheck=1")
    Page<Emergency> findPartnerCheckEmergency(Pageable pageable, Long pno);

    @Query("select e from Emergency e where e.balju.baljuNo=:baljuNo")
    List<Emergency> getEmengencyListByBalju(Long baljuNo);

    @EntityGraph(attributePaths = {"balju"})
    @Query("select e from Emergency e " +
            "where e.balju.contract.jodalPlan.proPlan.proplanNo=:proplanNo " +
            "and e.balju.contract.jodalPlan.material.materCode=:materCode")
    Page<Emergency> findEmergency(Pageable pageable, Long materCode, Long proplanNo);

    @Modifying
    @Transactional
    @EntityGraph(attributePaths = {"balju"})
    @Query("update Emergency e set e.emcheck=1 where e.emerNo=:emerNo")
    void updateEmergencyCheck(Long emerNo);
}
