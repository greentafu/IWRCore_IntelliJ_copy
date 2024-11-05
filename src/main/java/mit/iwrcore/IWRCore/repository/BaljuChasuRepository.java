package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.BaljuChasu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BaljuChasuRepository extends JpaRepository<BaljuChasu, Long> {
    @Query("select bc from BaljuChasu bc where bc.balju.baljuNo=:baljuNo order by bc.balNo asc")
    List<BaljuChasu> getBaljuChasuListByBaljuNo(Long baljuNo);

    @Query("select c, bc, b from Contract c " +
            "left join Balju b on (c.conNo=b.contract.conNo) " +
            "left join BaljuChasu bc on (b.baljuNo=bc.balju.baljuNo) " +
            "where b.baljuNo is not null and b.finCheck=0 and c.partner.pno=:pno")
    List<Object[]> modifyBalju(Long pno);
}
