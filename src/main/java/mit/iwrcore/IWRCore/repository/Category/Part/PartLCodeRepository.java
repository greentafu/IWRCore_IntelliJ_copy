package mit.iwrcore.IWRCore.repository.Category.Part;

import mit.iwrcore.IWRCore.entity.PartL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartLCodeRepository extends JpaRepository<PartL, Long> {
    @Query("select distinct l from PartL l " +
            "where l.partLcode!=2 " +
            "order by l.partLcode")
    List<PartL> getListPartL1();

    @Query("select distinct l from PartL l " +
            "where l.partLcode!=1 and l.partLcode!=2 " +
            "order by l.partLcode")
    List<PartL> getListPartL2();


    @Query("select l, l.partLcode from PartL l " +
            "where l.partLcode!=1 and l.partLcode!=2 " +
            "order by l.partLcode")
    Page<Object[]> getPagePartL(Pageable pageable);
}
