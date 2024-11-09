package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    @Query("select p from Plan p where p.product.manuCode=:manuCode")
    List<Plan> findPlanByProduct(Long manuCode);

    @Query("select p from Plan p where p.product.manuCode=:manuCode and p.line.lineName=:lineName")
    Plan findLineByLine(Long manuCode, String lineName);
}
