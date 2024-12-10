package mit.iwrcore.IWRCore.repository.Category.Pro;

import mit.iwrcore.IWRCore.entity.ProS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProSCodeRepository extends JpaRepository<ProS, Long> {
    @Query("select distinct s from ProS s " +
            "left join ProM m on (m.proMcode=s.proM.proMcode) " +
            "left join ProL l on (l.proLcode=s.proM.proL.proLcode) " +
            "where l.proLcode!=1 " +
            "order by s.proScode")
    List<ProS> getListProS1();

    @Query("select distinct s from ProS s " +
            "left join ProM m on (m.proMcode=s.proM.proMcode) " +
            "left join ProL l on (l.proLcode=s.proM.proL.proLcode) " +
            "where l.proLcode=:code " +
            "order by s.proScode")
    List<ProS> getListProSByProL(Long code);

    @Query("select distinct s from ProS s " +
            "left join ProM m on (m.proMcode=s.proM.proMcode) " +
            "where m.proMcode=:code " +
            "order by s.proScode")
    List<ProS> getListProSByProM(Long code);

    @Query("select s, m, l from ProS s " +
            "left join ProM m on (m.proMcode=s.proM.proMcode) " +
            "left join ProL l on (l.proLcode=s.proM.proL.proLcode) " +
            "where l.proLcode!=1 " +
            "order by l.proLcode, m.proMcode, s.proScode")
    Page<Object[]> getPageProS(Pageable pageable);
}
