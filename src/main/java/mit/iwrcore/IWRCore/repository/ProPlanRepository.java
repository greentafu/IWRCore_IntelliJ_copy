package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.ProPlan;
import mit.iwrcore.IWRCore.repositoryDSL.ProPlanRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProPlanRepository extends JpaRepository<ProPlan, Long>, ProPlanRepositoryCustom {
    @Query("select count(p) from ProPlan p where p.product.manuCode=:manuCode")
    Long checkProPlan(Long manuCode);
}
