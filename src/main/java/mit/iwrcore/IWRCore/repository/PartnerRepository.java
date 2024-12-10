package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Partner;
import mit.iwrcore.IWRCore.repositoryDSL.PartnerRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface PartnerRepository extends JpaRepository<Partner, Long>, QuerydslPredicateExecutor<Partner>, PartnerRepositoryCustom {
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Partner p where p.id =:id")
    Optional<Partner> findByID(String id);

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Partner p where p.pno=:pno or p.id =:id or p.registrationNumber=:reg")
    Partner findPartner(Long pno, String id, String reg);

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Partner p where p.partS.partScode=:code")
    List<Partner> getPartnerByCategoryS(Long code);

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Partner p where p.partS.partM.partMcode=:code")
    List<Partner> getPartnerByCategoryM(Long code);

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Partner p where p.partS.partM.partL.partLcode=:code")
    List<Partner> getPartnerByCategoryL(Long code);
}
