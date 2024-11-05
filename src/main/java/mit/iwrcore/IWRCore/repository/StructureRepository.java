package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Structure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StructureRepository extends JpaRepository<Structure,Long> {
    @Query("select s, p from Structure s " +
            "join Product p on (s.product.manuCode=p.manuCode) " +
            "where s.product.manuCode=:manuCode")
    List<Object[]> findByProduct_ManuCode(Long manuCode);
}
