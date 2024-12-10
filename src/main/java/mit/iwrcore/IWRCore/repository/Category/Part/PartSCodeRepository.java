package mit.iwrcore.IWRCore.repository.Category.Part;

import mit.iwrcore.IWRCore.entity.PartS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartSCodeRepository extends JpaRepository<PartS, Long> {
    @Query("select distinct s from PartS s " +
            "left join PartM m on (m.partMcode=s.partM.partMcode) " +
            "left join PartL l on (l.partLcode=s.partM.partL.partLcode) " +
            "where l.partLcode!=2 " +
            "order by s.partScode")
    List<PartS> getListPartS1();

    @Query("select distinct s from PartS s " +
            "left join PartM m on (m.partMcode=s.partM.partMcode) " +
            "left join PartL l on (l.partLcode=s.partM.partL.partLcode) " +
            "where l.partLcode!=1 and l.partLcode!=2 " +
            "order by s.partScode")
    List<PartS> getListPartS2();

    @Query("select distinct s from PartS s " +
            "left join PartM m on (m.partMcode=s.partM.partMcode) " +
            "left join PartL l on (l.partLcode=s.partM.partL.partLcode) " +
            "where s.partM.partL.partLcode=:code " +
            "order by s.partScode")
    List<PartS> getListPartSByPartL(Long code);

    @Query("select distinct s from PartS s " +
            "left join PartM m on (m.partMcode=s.partM.partMcode) " +
            "where m.partMcode=:code " +
            "order by s.partScode")
    List<PartS> getListPartSByPartM(Long code);

    @Query("select s, m, l from PartS s " +
            "left join PartM m on (m.partMcode=s.partM.partMcode) " +
            "left join PartL l on (l.partLcode=s.partM.partL.partLcode) " +
            "where l.partLcode!=1 and l.partLcode!=2 " +
            "order by l.partLcode, m.partMcode, s.partScode")
    Page<Object[]> getPagePartS(Pageable pageable);
}
