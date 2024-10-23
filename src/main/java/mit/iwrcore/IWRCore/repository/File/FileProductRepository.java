package mit.iwrcore.IWRCore.repository.File;

import mit.iwrcore.IWRCore.entity.FileProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileProductRepository extends JpaRepository<FileProduct, String> {
    @Query("select f from FileProduct f where f.product.manuCode=:manuCode")
    List<FileProduct> getProductFileListByManuCode(Long manuCode);
}
