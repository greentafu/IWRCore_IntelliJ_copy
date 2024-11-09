package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.JodalPlan;
import mit.iwrcore.IWRCore.repositoryDSL.JodalPlanRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JodalPlanRepository extends JpaRepository<JodalPlan, Long>, JodalPlanRepositoryCustom {
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