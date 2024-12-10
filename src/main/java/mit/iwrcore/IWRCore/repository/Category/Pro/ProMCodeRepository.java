package mit.iwrcore.IWRCore.repository.Category.Pro;

import mit.iwrcore.IWRCore.entity.ProM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProMCodeRepository extends JpaRepository<ProM, Long> {
    @Query("select distinct m from ProM m " +
            "left join ProL l on (l.proLcode=m.proL.proLcode) " +
            "where l.proLcode!=1 " +
            "order by m.proMcode")
    List<ProM> getListProM1();

    @Query("select distinct m from ProM m " +
            "left join ProL l on (l.proLcode=m.proL.proLcode) " +
            "where l.proLcode=:code " +
            "order by m.proMcode")
    List<ProM> getListProMByProL(Long code);

    @Query("select m, l from ProM m " +
            "left join ProL l on (l.proLcode=m.proL.proLcode) " +
            "where l.proLcode!=1 " +
            "order by l.proLcode, m.proMcode")
    Page<Object[]> getPageProM(Pageable pageable);
}
