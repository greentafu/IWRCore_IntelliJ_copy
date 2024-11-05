package mit.iwrcore.IWRCore.repository;

import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.JodalPlan;
import mit.iwrcore.IWRCore.repositoryDSL.JodalPlanRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;

public interface JodalPlanRepository extends JpaRepository<JodalPlan, Long>, JodalPlanRepositoryCustom {
    @Query("select j, p, pp from JodalPlan j " +
            "join Product p on (j.proPlan.product.manuCode=p.manuCode) join ProPlan pp on (j.proPlan.proplanNo=pp.proplanNo) " +
            "where j.joNo=:joNo")
    List<Object[]> getJodalPlan(Long joNo);
    @Query("select count(j) from JodalPlan j where j.joNo not in (select jc.jodalPlan.joNo from JodalChasu jc)")
    Long newNoneJodalPlanCount();
    List<JodalPlan> findByProPlanProplanNo(Long proplanNo);
    @EntityGraph(attributePaths = {"proPlan"})
    @Query("select j, sum(jc.joNum) from JodalPlan j " +
            "join JodalChasu jc on (j.joNo=jc.jodalPlan.joNo) " +
            "where j.joNo not in (select c.jodalPlan.joNo from Contract c) and j.joNo in (select jc.jodalPlan.joNo from JodalChasu jc) " +
            "group by j")
    List<Object[]> noneContractJodalPlanList();
    // 계약서> 선택한 조달계획
    @EntityGraph(attributePaths = {"proPlan"})
    @Query("select j, sum(jc.joNum) from JodalPlan j " +
            "join JodalChasu jc on (j.joNo=jc.jodalPlan.joNo) " +
            "where j.joNo=:joNo " +
            "group by j")
    List<Object[]> selectedJodalPlan(Long joNo);
}
