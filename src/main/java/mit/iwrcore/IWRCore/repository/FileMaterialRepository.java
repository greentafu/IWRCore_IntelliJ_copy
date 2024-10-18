package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.FileMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileMaterialRepository extends JpaRepository<FileMaterial, String> {
    @Query("select f from FileMaterial f where f.material.materCode=:materCode")
    List<FileMaterial> getMaterialFileListByMaterCode(Long materCode);
}
