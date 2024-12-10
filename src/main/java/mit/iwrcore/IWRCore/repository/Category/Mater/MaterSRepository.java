package mit.iwrcore.IWRCore.repository.Category.Mater;

import mit.iwrcore.IWRCore.entity.MaterS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterSRepository extends JpaRepository<MaterS, Long> {
    @Query("select distinct s from MaterS s " +
            "left join MaterM m on (m.materMcode=s.materM.materMcode) " +
            "left join MaterL l on (l.materLcode=s.materM.materL.materLcode) " +
            "where l.materLcode!=1 " +
            "order by s.materScode")
    List<MaterS> getListMaterS1();

    @Query("select distinct s from MaterS s " +
            "left join MaterM m on (m.materMcode=s.materM.materMcode) " +
            "left join MaterL l on (l.materLcode=s.materM.materL.materLcode) " +
            "where l.materLcode=:code " +
            "order by s.materScode")
    List<MaterS> getListMaterByMaterL(Long code);

    @Query("select distinct s from MaterS s " +
            "left join MaterM m on (m.materMcode=s.materM.materMcode) " +
            "where m.materMcode=:code " +
            "order by s.materScode")
    List<MaterS> getListMaterSByMaterM(Long code);

    @Query("select s, m, l from MaterS s " +
            "left join MaterM m on (m.materMcode=s.materM.materMcode) " +
            "left join MaterL l on (l.materLcode=s.materM.materL.materLcode) " +
            "where l.materLcode!=1 " +
            "order by l.materLcode, m.materMcode, s.materScode")
    Page<Object[]> getPageMaterS(Pageable pageable);
}
