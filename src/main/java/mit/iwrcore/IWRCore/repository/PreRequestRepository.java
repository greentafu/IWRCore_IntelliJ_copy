package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.PreRequest;
import mit.iwrcore.IWRCore.repositoryDSL.PreRequestRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PreRequestRepository extends JpaRepository<PreRequest, Long>, PreRequestRepositoryCustom {
    @Modifying
    @Transactional
    @Query("update PreRequest r set r.allCheck=1 where r.preReqCode=:preReqCode")
    void updateAllCheck(Long preReqCode);
}
