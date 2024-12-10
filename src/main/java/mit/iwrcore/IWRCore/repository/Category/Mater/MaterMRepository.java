package mit.iwrcore.IWRCore.repository.Category.Mater;

import mit.iwrcore.IWRCore.entity.MaterM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterMRepository extends JpaRepository<MaterM, Long> {
    @Query("select distinct m from MaterM m " +
            "left join MaterL l on (l.materLcode=m.materL.materLcode) " +
            "where l.materLcode!=1 " +
            "order by m.materMcode")
    List<MaterM> getListMaterM1();

    @Query("select distinct m from MaterM m " +
            "left join MaterL l on (l.materLcode=m.materL.materLcode) " +
            "where l.materLcode=:code " +
            "order by m.materMcode")
    List<MaterM> getListMaterMByMaterL(Long code);

    @Query("select m, l from MaterM m " +
            "left join MaterL l on (l.materLcode=m.materL.materLcode) " +
            "where l.materLcode!=1 " +
            "order by l.materLcode, m.materMcode")
    Page<Object[]> getPageMaterM(Pageable pageable);
}
