package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.GumsuChasu;
import mit.iwrcore.IWRCore.repositoryDSL.GumsuChasuRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GumsuChasuRepository extends JpaRepository<GumsuChasu,Long>, GumsuChasuRepositoryCustom {
    @Query("select gc from GumsuChasu gc where gc.gumsu.gumsuNo=:gumsuNo order by gc.gcnum asc")
    List<GumsuChasu> getGumsuChasuListByGumsu(Long gumsuNo);

    @Query("select g from GumsuChasu g where g.gumsu.balju.baljuNo=:baljuNo")
    List<GumsuChasu> getGumsuChasuByBaljuNo(Long baljuNo);
}
