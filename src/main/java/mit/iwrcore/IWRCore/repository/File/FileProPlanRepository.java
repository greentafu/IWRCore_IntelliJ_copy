package mit.iwrcore.IWRCore.repository.File;

import mit.iwrcore.IWRCore.entity.FileMaterial;
import mit.iwrcore.IWRCore.entity.FileProPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileProPlanRepository extends JpaRepository<FileProPlan, String> {
    @Query("select f from FileProPlan f where f.proPlan.proplanNo=:proplanNo")
    List<FileProPlan> getProPlanFileListByProPlanNo(Long proplanNo);
}
