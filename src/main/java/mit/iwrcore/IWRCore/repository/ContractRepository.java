package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Contract;
import mit.iwrcore.IWRCore.repositoryDSL.ContractRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {
    @Query("select c, jc from Contract c " +
            "left join Balju b on (c.conNo=b.contract.conNo) " +
            "left join JodalChasu jc on (c.jodalPlan.joNo=jc.jodalPlan.joNo) " +
            "where b.baljuNo is null and c.partner.pno=:pno")
    List<Object[]> newOrderContract(Long pno);

    @Query("select c.conDate from Contract c")
    List<LocalDateTime> conDateList();
}
