package mit.iwrcore.IWRCore.repositoryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProPlanRepositoryImpl implements ProPlanRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    ProPlanRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional
    public Page<Object[]> findProPlanByCustomQuery(PageRequestDTO2 requestDTO){
        QProPlan qProPlan=QProPlan.proPlan;
        QProduct qProduct=QProduct.product;
        QProL qProL=QProL.proL;
        QProM qProM=QProM.proM;
        QProS qProS=QProS.proS;

        QContract qContract=QContract.contract;
        QJodalPlan qJodalPlan=QJodalPlan.jodalPlan;
        QJodalChasu qJodalChasu=QJodalChasu.jodalChasu;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getProL2() != null) { builder.and(qProS.proM.proL.proLcode.eq(requestDTO.getProL2())); }
        if (requestDTO.getProM2() != null) { builder.and(qProS.proM.proMcode.eq(requestDTO.getProM2())); }
        if (requestDTO.getProS2() != null) { builder.and(qProS.proScode.eq(requestDTO.getProS2())); }
        if (requestDTO.getProductSearch2()!=null){
            builder.and(qProduct.name.containsIgnoreCase(requestDTO.getProductSearch2())
                    .or(qProduct.supervisor.containsIgnoreCase(requestDTO.getProductSearch2())));
        }

        BooleanBuilder havingBuilder=new BooleanBuilder();

        if (requestDTO.getProplanProgress2() != null) {
            if (requestDTO.getProplanProgress2() == 0) { havingBuilder.and(qContract.count().eq(0L)).and(qJodalChasu.count().eq(0L)); }
            if (requestDTO.getProplanProgress2() == 1) { havingBuilder.and(qContract.count().eq(0L)).and(qJodalChasu.count().ne(0L)); }
            if (requestDTO.getProplanProgress2() == 2) { havingBuilder.and(qContract.count().ne(0L)); }
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage2()-1, requestDTO.getSize2());
        List<Tuple> tupleList = queryFactory
                .select(qProPlan, qContract.count(), qJodalChasu.count())
                .from(qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .leftJoin(qProPlan.jodalPlans, qJodalPlan)
                .leftJoin(qJodalPlan.contract, qContract)
                .leftJoin(qJodalPlan.jodalChasus, qJodalChasu)
                .where(builder)
                .groupBy(qProPlan.proplanNo)
                .having(havingBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProPlan.proplanNo.desc())
                .fetch();

        long total = queryFactory
                .select(qProPlan, qContract.count(), qJodalChasu.count())
                .from(qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .leftJoin(qProPlan.jodalPlans, qJodalPlan)
                .leftJoin(qJodalPlan.contract, qContract)
                .leftJoin(qJodalPlan.jodalChasus, qJodalChasu)
                .where(builder)
                .groupBy(qProPlan.proplanNo)
                .having(havingBuilder)
                .fetchCount();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qProPlan),        // Adjust according to your actual selection
                        tuple.get(qContract.count()),
                        tuple.get(qJodalChasu.count())
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }

    @Override
    @Transactional
    public Page<ProPlan> getPlanProPlan(PageRequestDTO requestDTO){
        QProPlan qProPlan=QProPlan.proPlan;
        QProduct qProduct=QProduct.product;
        QProL qProL=QProL.proL;
        QProM qProM=QProM.proM;
        QProS qProS=QProS.proS;

        LocalDateTime localDateTime=LocalDateTime.now();

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getProL() != null) { builder.and(qProS.proM.proL.proLcode.eq(requestDTO.getProL())); }
        if (requestDTO.getProM() != null) { builder.and(qProS.proM.proMcode.eq(requestDTO.getProM())); }
        if (requestDTO.getProS() != null) { builder.and(qProS.proScode.eq(requestDTO.getProS())); }
        if (requestDTO.getProductSearch()!=null){
            builder.and(qProduct.name.containsIgnoreCase(requestDTO.getProductSearch())
                    .or(qProduct.supervisor.containsIgnoreCase(requestDTO.getProductSearch())));
        }
        builder.and(qProPlan.finCheck.eq(0L));
        builder.and(qProPlan.endDate.after(localDateTime));

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<ProPlan> proPlanlList = queryFactory
                .select(qProPlan)
                .from(qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProPlan.proplanNo.desc())
                .fetch();

        long total = queryFactory
                .select(qProPlan)
                .from(qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(proPlanlList, pageable, total);
    }
}
