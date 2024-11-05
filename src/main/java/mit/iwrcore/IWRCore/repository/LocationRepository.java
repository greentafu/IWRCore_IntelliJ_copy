package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Location;
import mit.iwrcore.IWRCore.repositoryDSL.LocationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long>, LocationRepositoryCustom {
}
