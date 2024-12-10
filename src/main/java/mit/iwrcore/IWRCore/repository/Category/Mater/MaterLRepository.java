package mit.iwrcore.IWRCore.repository.Category.Mater;

import mit.iwrcore.IWRCore.entity.MaterL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterLRepository extends JpaRepository<MaterL, Long> {
    @Query("select l from MaterL l " +
            "where l.materLcode!=1 " +
            "order by l.materLcode")
    List<MaterL> getListMaterL1();

    @Query("select l, l.materLcode from MaterL l " +
            "where l.materLcode!=1 " +
            "order by l.materLcode")
    Page<Object[]> getPageMaterL(Pageable pageable);
}
