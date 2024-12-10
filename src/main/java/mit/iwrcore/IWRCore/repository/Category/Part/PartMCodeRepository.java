package mit.iwrcore.IWRCore.repository.Category.Part;

import mit.iwrcore.IWRCore.entity.PartM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartMCodeRepository extends JpaRepository<PartM, Long> {
    @Query("select distinct m from PartM m " +
            "left join PartL l on (l.partLcode=m.partL.partLcode) " +
            "where l.partLcode!=2 " +
            "order by m.partMcode")
    List<PartM> getListPartM1();

    @Query("select distinct m from PartM m " +
            "left join PartL l on (l.partLcode=m.partL.partLcode) " +
            "where l.partLcode!=1 and l.partLcode!=2 " +
            "order by m.partMcode")
    List<PartM> getListPartM2();

    @Query("select distinct m from PartM m " +
            "left join PartL l on (l.partLcode=m.partL.partLcode) " +
            "where l.partLcode=:code " +
            "order by m.partMcode")
    List<PartM> getListPartMByPartL(Long code);



    @Query("select m, l from PartM m " +
            "left join PartL l on (l.partLcode=m.partL.partLcode) " +
            "where l.partLcode!=1 and l.partLcode!=2 " +
            "order by l.partLcode, m.partMcode")
    Page<Object[]> getPagePartM(Pageable pageable);
}
