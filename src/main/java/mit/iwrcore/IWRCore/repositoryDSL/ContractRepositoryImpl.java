package mit.iwrcore.IWRCore.repositoryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

public class ContractRepositoryImpl implements ContractRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    ContractRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional
    public Page<Object[]> findContractByCustomQuery(PageRequestDTO2 requestDTO){
        QJodalPlan qJodalPlan=QJodalPlan.jodalPlan;
        QProPlan qProPlan=QProPlan.proPlan;
        QJodalChasu qJodalChasu=QJodalChasu.jodalChasu;
        QContract qContract=QContract.contract;

        QMaterial qMaterial=QMaterial.material;
        QMaterL qMaterL=QMaterL.materL;
        QMaterM qMaterM=QMaterM.materM;
        QMaterS qMaterS=QMaterS.materS;

        QProduct qProduct=QProduct.product;
        QProL qProL=QProL.proL;
        QProM qProM=QProM.proM;
        QProS qProS=QProS.proS;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getProL2() != null) { builder.and(qProS.proM.proL.proLcode.eq(requestDTO.getProL2())); }
        if (requestDTO.getProM2() != null) { builder.and(qProS.proM.proMcode.eq(requestDTO.getProM2())); }
        if (requestDTO.getProS2() != null) { builder.and(qProS.proScode.eq(requestDTO.getProS2())); }
        if (requestDTO.getProductSearch2()!=null){
            builder.and(qProduct.name.containsIgnoreCase(requestDTO.getProductSearch2())
                    .or(qProduct.supervisor.containsIgnoreCase(requestDTO.getProductSearch2())));
        }

        if (requestDTO.getMaterL2() != null) { builder.and(qMaterS.materM.materL.materLcode.eq(requestDTO.getMaterL2())); }
        if (requestDTO.getMaterM2() != null) { builder.and(qMaterS.materM.materMcode.eq(requestDTO.getMaterM2())); }
        if (requestDTO.getMaterS2() != null) { builder.and(qMaterS.materScode.eq(requestDTO.getMaterS2())); }
        if (requestDTO.getMaterialSearch2()!=null){
            builder.and(qMaterial.name.containsIgnoreCase(requestDTO.getMaterialSearch2())
                    .or(qMaterial.standard.containsIgnoreCase(requestDTO.getMaterialSearch2()))
                    .or(qMaterial.unit.containsIgnoreCase(requestDTO.getMaterialSearch2())));
        }

        if(requestDTO.getProgressContract2()!=null){
            if(requestDTO.getProgressContract2()==0) { builder.and(qContract.isNull()); }
            if(requestDTO.getProgressContract2()==1) { builder.and(qContract.isNotNull()); }
        }

        BooleanBuilder havingBuilder=new BooleanBuilder();
        havingBuilder.and(qJodalChasu.count().ne(0L));

        Pageable pageable= PageRequest.of(requestDTO.getPage2()-1, requestDTO.getSize2());
        List<Tuple> tupleList = queryFactory
                .select(qJodalPlan, qContract, qJodalChasu, qJodalChasu.jcnum.sum())
                .from(qJodalPlan)
                .leftJoin(qJodalPlan.proPlan, qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .leftJoin(qJodalPlan.jodalChasus, qJodalChasu)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qJodalPlan.contracts, qContract)
                .where(builder)
                .groupBy(qJodalPlan.joNo)
                .having(havingBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qJodalPlan.joNo.desc())
                .fetch();

        long total = queryFactory
                .select(qJodalPlan)
                .from(qJodalPlan)
                .leftJoin(qJodalPlan.proPlan, qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .leftJoin(qJodalPlan.jodalChasus, qJodalChasu)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qJodalPlan.contracts, qContract)
                .where(builder)
                .groupBy(qJodalPlan.joNo)
                .having(havingBuilder)
                .fetchCount();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qJodalPlan),
                        tuple.get(qContract),
                        tuple.get(qJodalChasu),
                        tuple.get(qJodalChasu.jcnum.sum())
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }
}
