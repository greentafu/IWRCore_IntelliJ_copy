package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.LineStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LineStructureRepository extends JpaRepository<LineStructure, Long> {
    @Query("select s from LineStructure s where s.proPlan.proplanNo=:proplanNo")
    List<LineStructure> findByProPlanNo(Long proplanNo);
}
