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

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getProL() != null) { builder.and(qProS.proM.proL.proLcode.eq(requestDTO.getProL())); }
        if (requestDTO.getProM() != null) { builder.and(qProS.proM.proMcode.eq(requestDTO.getProM())); }
        if (requestDTO.getProS() != null) { builder.and(qProS.proScode.eq(requestDTO.getProS())); }
        if (requestDTO.getProductSearch()!=null){
            builder.and(qProduct.name.containsIgnoreCase(requestDTO.getProductSearch())
                    .or(qProduct.supervisor.containsIgnoreCase(requestDTO.getProductSearch())));
        }
        builder.and(qProPlan.finCheck.eq(0L));

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

//    @Override
//    @Transactional
//    public List<Object[]> getStructureStock(Long proplanNo){
//        QProPlan qProPlan=QProPlan.proPlan;
//        QStructure qStructure=QStructure.structure;
//        QMaterial qMaterial=QMaterial.material;
//        QShipment qShipment=QShipment.shipment;
//        QRequest qRequest=QRequest.request;
//
//        QJodalPlan qJodalPlan=QJodalPlan.jodalPlan;
//        QContract qContract=QContract.contract;
//
//        BooleanBuilder builder=new BooleanBuilder();
//
//        builder.and( qProPlan.proplanNo.eq(proplanNo) );
//
//        List<Tuple> tupleList = queryFactory
//                .select(qProPlan, qStructure, qShipment.shipNum.sum(), qRequest.requestNum.sum())
//                .from(qProPlan)
//                .leftJoin(qStructure).on(qStructure.product.eq(qProPlan.product))
//                .leftJoin(qMaterial).on(qMaterial.structures.contains(qStructure))
//                .leftJoin(qRequest).on(qRequest.material.eq(qMaterial).and(qRequest.reqCheck.eq(1L)))
//                .leftJoin(qJodalPlan).on(qJodalPlan.material.eq(qMaterial))
//                .leftJoin(qContract).on(qContract.jodalPlan.eq(qJodalPlan))
//                .leftJoin(qShipment).on(qShipment.balju.contract.eq(qContract).and(qShipment.receiveCheck.eq(1L)))
//                .where(builder)
//                .groupBy(qMaterial.materCode)
//                .orderBy(qStructure.sno.desc())
//                .fetch();
//
//        List<Object[]> objectList = tupleList.stream()
//                .map(tuple -> new Object[]{
//                        tuple.get(qProPlan),
//                        tuple.get(qStructure),
//                        tuple.get(qShipment.shipNum.sum()),
//                        tuple.get(qRequest.requestNum.sum())
//                })
//                .collect(Collectors.toList());
//
//        return objectList;
//    }
}
