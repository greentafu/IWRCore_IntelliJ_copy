package mit.iwrcore.IWRCore.repository.File;

import mit.iwrcore.IWRCore.entity.FileContract;
import mit.iwrcore.IWRCore.entity.FileMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileContractRepository extends JpaRepository<FileContract, String> {
    @Query("select f from FileContract f where f.contract.conNo=:conNo")
    List<FileContract> getContractFileListByConNo(Long conNo);
}
