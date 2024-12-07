package mit.iwrcore.IWRCore.repositoryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Coalesce;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
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
                .select(qJodalPlan, qContract, qJodalChasu, qJodalChasu.joNum.sum())
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
                .leftJoin(qJodalPlan.contract, qContract)
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
                .leftJoin(qJodalPlan.contract, qContract)
                .where(builder)
                .groupBy(qJodalPlan.joNo)
                .having(havingBuilder)
                .fetchCount();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qJodalPlan),
                        tuple.get(qContract),
                        tuple.get(qJodalChasu),
                        tuple.get(qJodalChasu.joNum.sum())
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }

    @Override
    @Transactional
    public Page<Object[]> findContractByCustomQuery2(PageRequestDTO requestDTO){
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

        builder.and(qContract.isNull());

        BooleanBuilder havingBuilder=new BooleanBuilder();
        havingBuilder.and(qJodalChasu.count().ne(0L));

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Tuple> tupleList = queryFactory
                .select(qJodalPlan, qContract, qJodalChasu, qJodalChasu.joNum.sum())
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
                .leftJoin(qJodalPlan.contract, qContract)
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
                .leftJoin(qJodalPlan.contract, qContract)
                .where(builder)
                .groupBy(qJodalPlan.joNo)
                .having(havingBuilder)
                .fetchCount();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qJodalPlan),
                        tuple.get(qContract),
                        tuple.get(qJodalChasu),
                        tuple.get(qJodalChasu.joNum.sum())
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }

    @Override
    @Transactional
    public Page<Contract> partnerContractPage(PageRequestDTO requestDTO){
        QContract qContract=QContract.contract;
        QPartner qPartner=QPartner.partner;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getPno()!=null) { builder.and(qPartner.pno.eq(requestDTO.getPno())); }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Contract> contractlList = queryFactory
                .select(qContract)
                .from(qContract)
                .leftJoin(qContract.partner, qPartner)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qContract.conDate.desc())
                .fetch();

        long total = queryFactory
                .select(qContract)
                .from(qContract)
                .leftJoin(qContract.partner, qPartner)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(contractlList, pageable, total);
    }

    @Override
    @Transactional
    public Page<Object[]> stockQuantityPage(PageRequestDTO requestDTO){
        QProduct qProduct=QProduct.product;
        QStructure qStructure=QStructure.structure;
        QMaterial qMaterial=QMaterial.material;
        QMaterial qMaterial1=QMaterial.material;
        QShipment qShipment=QShipment.shipment;
        QRequest qRequest=QRequest.request;

        QContract qContract=QContract.contract;
        QContract qContract1=QContract.contract;
        QBalju qBalju=QBalju.balju;

        QProL qProL=QProL.proL;
        QProM qProM=QProM.proM;
        QProS qProS=QProS.proS;
        QMaterL qMaterL=QMaterL.materL;
        QMaterM qMaterM=QMaterM.materM;
        QMaterS qMaterS=QMaterS.materS;

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
            builder.and(qMaterial1.name.containsIgnoreCase(requestDTO.getMaterialSearch())
                    .or(qMaterial1.standard.containsIgnoreCase(requestDTO.getMaterialSearch()))
                    .or(qMaterial1.unit.containsIgnoreCase(requestDTO.getMaterialSearch())));
        }
        if (requestDTO.getBox()!=null) { builder.and(qMaterial1.box.boxCode.eq(requestDTO.getBox())); }

        JPAQuery<Material> subQuery=new JPAQuery<>()
                .select(qMaterial1).distinct()
                .from(qMaterial1)
                .leftJoin(qMaterial1.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qStructure).on(qStructure.material.eq(qMaterial1))
                .leftJoin(qStructure.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .where(builder);

        JPAQuery<Contract> subQueryContract=new JPAQuery<>()
                .select(qContract1)
                .from(qContract1)
                .where(qContract1.jodalPlan.material.eq(qMaterial));

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


        BooleanBuilder builder1=new BooleanBuilder();
        builder1.and( qMaterial.in(subQuery) );
        if (requestDTO.getStockStatus()!=null){
            if(requestDTO.getStockStatus()==0L){
                builder1.and(subQueryShipment.isNull())
                        .or(subQueryShipment.isNotNull().and(subQueryRequest.isNotNull())
                                .and(subQueryShipment.loe(subQueryRequest))
                        );
            }else{
                builder1.and(subQueryShipment.isNotNull().and(subQueryRequest.isNull()))
                        .or(subQueryShipment.isNotNull().and(subQueryRequest.isNotNull())
                                .and(subQueryShipment.goe(subQueryRequest))
                        );
            }
        }
        BooleanBuilder havingBuilder=new BooleanBuilder();
        if (requestDTO.getBaljuStatus()!=null){
            if(requestDTO.getBaljuStatus()==0L) havingBuilder.and(qBalju.countDistinct().eq(0L));
            else havingBuilder.and(qBalju.countDistinct().ne(0L));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Tuple> tupleList = queryFactory
                .select(qMaterial, qContract.conNo.max(), subQueryShipment, subQueryRequest, qBalju.countDistinct())
                .from(qMaterial)
                .leftJoin(qContract).on(qContract.jodalPlan.material.eq(qMaterial))
                .leftJoin(qBalju).on(qBalju.contract.in(subQueryContract))
                .where(builder1)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(qMaterial.materCode)
                .having(havingBuilder)
                .orderBy(qMaterial.materCode.desc())
                .fetch();

        long total = queryFactory
                .select(qMaterial, qContract.conNo.max(), subQueryShipment, subQueryRequest, qBalju.countDistinct())
                .from(qMaterial)
                .leftJoin(qContract).on(qContract.jodalPlan.material.eq(qMaterial))
                .leftJoin(qBalju).on(qBalju.contract.in(subQueryContract))
                .where(builder1)
                .groupBy(qMaterial.materCode)
                .having(havingBuilder)
                .orderBy(qMaterial.materCode.desc())
                .fetchCount();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qMaterial),
                        tuple.get(qContract.conNo.max()),
                        tuple.get(subQueryShipment),
                        tuple.get(subQueryRequest),
                        tuple.get(qBalju.countDistinct())
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }

    @Override
    @Transactional
    public Page<Object[]> stockDetailPage(PageRequestDTO requestDTO){
        QMaterial qMaterial=QMaterial.material;
        QContract qContract=QContract.contract;
        QContract qContract1=QContract.contract;
        QShipment qShipment=QShipment.shipment;
        QRequest qRequest=QRequest.request;

        BooleanBuilder builder=new BooleanBuilder();
        if(requestDTO.getSelectedYear()!=null) builder.and(qContract.conDate.year().eq(requestDTO.getSelectedYear()));

        JPAQuery<Contract> subQuery=new JPAQuery<>()
                .select(qContract1).distinct()
                .from(qContract1)
                .leftJoin(qContract1.jodalPlan.material, qMaterial)
                .where(qMaterial.materCode.eq(requestDTO.getMaterCode()));
        builder.and(qContract.in(subQuery));

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Tuple> tupleList = queryFactory
                .select(qContract.conNo.max(), qShipment.shipNum.sum(), qRequest.requestNum.sum())
                .from(qContract)
                .leftJoin(qShipment).distinct().on(qShipment.balju.contract.in(subQuery)
                        .and(qShipment.receiveCheck.eq(1L)))
                .leftJoin(qRequest).on(qRequest.material.eq(qContract.jodalPlan.material)
                        .and(qRequest.releaseDate.isNotNull())
                        .and(qRequest.releaseDate.yearMonth().loe(qContract.conDate.yearMonth())))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(qContract.conDate.yearMonth())
                .orderBy(qContract.conDate.yearMonth().desc())
                .fetch();

        long total = queryFactory
                .select(qContract.conNo.max(), qShipment.shipNum.sum(), qRequest.requestNum.sum())
                .from(qContract)
                .leftJoin(qShipment).distinct().on(qShipment.balju.contract.in(subQuery)
                        .and(qShipment.receiveCheck.eq(1L)))
                .leftJoin(qRequest).on(qRequest.material.eq(qContract.jodalPlan.material)
                        .and(qRequest.releaseDate.isNotNull())
                        .and(qRequest.releaseDate.yearMonth().loe(qContract.conDate.yearMonth())))
                .where(builder)
                .groupBy(qContract.conDate.yearMonth())
                .orderBy(qContract.conDate.yearMonth().desc())
                .fetchCount();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qContract.conNo.max()),
                        tuple.get(qShipment.shipNum.sum()),
                        tuple.get(qRequest.requestNum.sum())
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }
}
