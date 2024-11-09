package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Gumsu;
import mit.iwrcore.IWRCore.repositoryDSL.GumsuRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GumsuRepository extends JpaRepository<Gumsu,Long>, GumsuRepositoryCustom {
    @Query("select g.make from Gumsu g where g.balju.baljuNo=:baljuNo")
    Long quantityMake(Long baljuNo);

    @Query("select g from Gumsu g where g.balju.baljuNo=:baljuNo")
    Gumsu getGumsuByBaljuNo(Long baljuNo);

    // 검수차수> 회사별 검수차수 설정 가능 발주 목록
    @EntityGraph(attributePaths = {"balju"})
    @Query("select b, bc from Balju b " +
            "left join BaljuChasu bc on(bc.balju.baljuNo=b.baljuNo) " +
            "where b.baljuNo not in (select g.balju.baljuNo from Gumsu g) and b.contract.partner.pno=:pno")
    List<Object[]> getNoneGumsu(Long pno);
}
