package mit.iwrcore.IWRCore.repository.Category.Pro;

import mit.iwrcore.IWRCore.entity.ProL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface  ProLCodeRepository extends JpaRepository<ProL, Long> {
    @Query("select l from ProL l " +
            "where l.proLcode!=1 " +
            "order by l.proLcode")
    List<ProL> getListProL1();

    @Query("select l, l.proLcode from ProL l " +
            "where l.proLcode!=1 " +
            "order by l.proLcode")
    Page<Object[]> getPageProL(Pageable pageable);
}
