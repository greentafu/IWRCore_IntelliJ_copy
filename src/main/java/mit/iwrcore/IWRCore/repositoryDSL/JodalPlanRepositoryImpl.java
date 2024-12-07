package mit.iwrcore.IWRCore.repositoryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
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

public class JodalPlanRepositoryImpl implements JodalPlanRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    JodalPlanRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional
    public Page<JodalPlan> findJodalPlanByCustomQuery(PageRequestDTO requestDTO){
        QJodalPlan qJodalPlan=QJodalPlan.jodalPlan;
        QJodalChasu qJodalChasu=QJodalChasu.jodalChasu;
        QProPlan qProPlan=QProPlan.proPlan;

        QMaterial qMaterial=QMaterial.material;
        QMaterL qMaterL=QMaterL.materL;
        QMaterM qMaterM=QMaterM.materM;
        QMaterS qMaterS=QMaterS.materS;

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

        if (requestDTO.getMaterL() != null) { builder.and(qMaterS.materM.materL.materLcode.eq(requestDTO.getMaterL())); }
        if (requestDTO.getMaterM() != null) { builder.and(qMaterS.materM.materMcode.eq(requestDTO.getMaterM())); }
        if (requestDTO.getMaterS() != null) { builder.and(qMaterS.materScode.eq(requestDTO.getMaterS())); }
        if (requestDTO.getMaterialSearch()!=null){
            builder.and(qMaterial.name.containsIgnoreCase(requestDTO.getMaterialSearch())
                    .or(qMaterial.standard.containsIgnoreCase(requestDTO.getMaterialSearch()))
                    .or(qMaterial.unit.containsIgnoreCase(requestDTO.getMaterialSearch())));
        }

        BooleanBuilder havingBuilder=new BooleanBuilder();
        havingBuilder.and(qJodalChasu.count().eq(0L));

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<JodalPlan> jodalPlanList = queryFactory
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
                .where(builder)
                .groupBy(qJodalPlan.joNo)
                .having(havingBuilder)
                .fetchCount();

        return new PageImpl<>(jodalPlanList, pageable, total);
    }

    @Override
    @Transactional
    public List<Object[]> getStructureStock(Long proplanNo){
        QProPlan qProPlan=QProPlan.proPlan;
        QStructure qStructure=QStructure.structure;
        QMaterial qMaterial=QMaterial.material;
        QShipment qShipment=QShipment.shipment;
        QRequest qRequest=QRequest.request;

        QJodalPlan qJodalPlan=QJodalPlan.jodalPlan;
        QContract qContract=QContract.contract;

        BooleanBuilder builder=new BooleanBuilder();

        builder.and( qProPlan.proplanNo.eq(proplanNo) );

        JPAQuery<Contract> subQueryContract=new JPAQuery<>()
                .select(qContract)
                .from(qContract)
                .where(qContract.jodalPlan.material.eq(qMaterial));

        JPAQuery<Long> subQueryShipment=new JPAQuery<>()
                .select(qShipment.shipNum.sum())
                .from(qShipment)
                .where(qShipment.balju.contract.in(subQueryContract)
                        .and(qShipment.receiveCheck.eq(1L)));

        JPAQuery<Long> subQueryRequest=new JPAQuery<>()
                .select(qRequest.requestNum.sum()).distinct()
                .from(qRequest)
                .where(qRequest.material.eq(qMaterial)
                        .and(qRequest.releaseDate.isNotNull()));

        List<Tuple> tupleList = queryFactory
                .select(qProPlan, qStructure, qJodalPlan, subQueryShipment, subQueryRequest)
                .from(qProPlan)
                .leftJoin(qStructure).on(qStructure.product.eq(qProPlan.product))
                .leftJoin(qMaterial).on(qMaterial.structures.contains(qStructure))
                .leftJoin(qJodalPlan).on(qJodalPlan.material.eq(qMaterial))
                .where(builder)
                .groupBy(qMaterial.materCode)
                .orderBy(qStructure.sno.desc())
                .fetch();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qProPlan),
                        tuple.get(qStructure),
                        tuple.get(qJodalPlan),
                        tuple.get(subQueryShipment),
                        tuple.get(subQueryRequest)
                })
                .collect(Collectors.toList());

        return objectList;
    }

    @Override
    @Transactional
    public Page<Object[]> noneContractJodalPlan(PageRequestDTO2 requestDTO2){
        QJodalPlan qJodalPlan=QJodalPlan.jodalPlan;
        QJodalChasu qJodalChasu=QJodalChasu.jodalChasu;
        QContract qContract=QContract.contract;

        QMaterial qMaterial=QMaterial.material;
        QMaterL qMaterL=QMaterL.materL;
        QMaterM qMaterM=QMaterM.materM;
        QMaterS qMaterS=QMaterS.materS;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO2.getMaterL2() != null) { builder.and(qMaterS.materM.materL.materLcode.eq(requestDTO2.getMaterL2())); }
        if (requestDTO2.getMaterM2() != null) { builder.and(qMaterS.materM.materMcode.eq(requestDTO2.getMaterM2())); }
        if (requestDTO2.getMaterS2() != null) { builder.and(qMaterS.materScode.eq(requestDTO2.getMaterS2())); }
        if (requestDTO2.getMaterialSearch2()!=null){
            builder.and(qMaterial.name.containsIgnoreCase(requestDTO2.getMaterialSearch2())
                    .or(qMaterial.standard.containsIgnoreCase(requestDTO2.getMaterialSearch2()))
                    .or(qMaterial.unit.containsIgnoreCase(requestDTO2.getMaterialSearch2())));
        }

        if (requestDTO2.getJodalPlans()!=null) { builder.and(qJodalPlan.joNo.notIn(requestDTO2.getJodalPlans())); }

        BooleanBuilder havingBuilder=new BooleanBuilder();
        havingBuilder.and(qContract.count().eq(0L));
        havingBuilder.and(qJodalChasu.count().ne(0L));

        Pageable pageable= PageRequest.of(requestDTO2.getPage2()-1, requestDTO2.getSize2());
        List<Tuple> tupleList = queryFactory
                .select(qJodalPlan, qJodalChasu.joNum.sum())
                .from(qJodalPlan)
                .leftJoin(qJodalPlan.jodalChasus, qJodalChasu)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qContract).on(qContract.jodalPlan.eq(qJodalPlan))
                .where(builder)
                .groupBy(qJodalPlan.joNo)
                .having(havingBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qJodalPlan.joNo.desc())
                .fetch();

        long total = queryFactory
                .select(qJodalPlan, qJodalChasu.joNum.sum())
                .from(qJodalPlan)
                .leftJoin(qJodalPlan.jodalChasus, qJodalChasu)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qContract).on(qContract.jodalPlan.eq(qJodalPlan))
                .where(builder)
                .groupBy(qJodalPlan.joNo)
                .having(havingBuilder)
                .fetchCount();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qJodalPlan),
                        tuple.get(qJodalChasu.joNum.sum())
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }
}
