package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Material;
import mit.iwrcore.IWRCore.repositoryDSL.MaterialRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long>, QuerydslPredicateExecutor<Material>, MaterialRepositoryCustom {
    @Query("select m from Material m where m.materS.materScode=:code")
    List<Material> getMaterialByMaterS(Long code);

    @Query("select m from Material m where m.materS.materScode=:code")
    List<Material> getMaterialByCategoryS(Long code);

    @Query("select m from Material m where m.materS.materM.materMcode=:code")
    List<Material> getMaterialByCategoryM(Long code);

    @Query("select m from Material m where m.materS.materM.materL.materLcode=:code")
    List<Material> getMaterialByCategoryL(Long code);
}
