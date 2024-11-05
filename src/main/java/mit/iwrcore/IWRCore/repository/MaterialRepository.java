package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Material;
import mit.iwrcore.IWRCore.repositoryDSL.MaterialRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MaterialRepository extends JpaRepository<Material, Long>, QuerydslPredicateExecutor<Material>, MaterialRepositoryCustom {
}
