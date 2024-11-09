package mit.iwrcore.IWRCore.repository.File;

import mit.iwrcore.IWRCore.entity.FileMaterial;
import mit.iwrcore.IWRCore.entity.FileReturns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileReturnsRepository extends JpaRepository<FileReturns, String> {
    @Query("select f from FileReturns f where f.returns.reNO=:reNO")
    List<FileReturns> getReturnsFileListByReNO(Long reNO);
}
