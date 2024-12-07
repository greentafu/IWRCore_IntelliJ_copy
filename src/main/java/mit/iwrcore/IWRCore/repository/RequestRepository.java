package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request , Long> {
    @Query("select r from Request r where r.preRequest.preReqCode=:preCode order by r.material.materCode desc")
    List<Request> getRequestByPreRequest(Long preCode);

    @Modifying
    @Transactional
    @Query("update Request r set r.releaseDate=:inputDate where r.requstCode=:requstCode")
    void updateReqCheck(LocalDateTime inputDate, Long requstCode);

    @Query("select count(r) from Request r where r.preRequest.preReqCode=:preCode")
    Long getAllRequestCount(Long preCode);

    @Query("select count(r) from Request r where r.preRequest.preReqCode=:preCode and r.releaseDate is not null")
    Long getFinRequestCount(Long preCode);

    @Query("select count(r) from Request r where r.releaseDate is null")
    Long mainRequestCount();
}
