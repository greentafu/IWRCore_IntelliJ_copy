package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Structure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StructureRepository extends JpaRepository<Structure,Long> {
    @Query("select s from Structure s where s.product.manuCode=:manuCode")
    List<Structure> findByProduct_ManuCode(Long manuCode);
}
