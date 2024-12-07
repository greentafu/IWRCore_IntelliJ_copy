package mit.iwrcore.IWRCore.repositoryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

public class PreRequestRepositoryImpl implements PreRequestRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    PreRequestRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Object[]> requestPage(PageRequestDTO requestDTO){
        QPreRequest qPreRequest=QPreRequest.preRequest;
        QRequest qRequest=QRequest.request;
        QRequest qRequest1=QRequest.request;
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
        if (requestDTO.getReleaseStatus() != null) {
            builder.and(qPreRequest.allCheck.eq(requestDTO.getReleaseStatus()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Tuple> tupleList = queryFactory
                .select(qPreRequest, qRequest.count(),
                        Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} IS NOT NULL THEN 1 ELSE NULL END)",
                                qRequest1.releaseDate))
                .from(qPreRequest)
                .leftJoin(qPreRequest.proPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .leftJoin(qRequest).on(qRequest.preRequest.eq(qPreRequest))
                .leftJoin(qRequest1).on(qRequest1.eq(qRequest))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(qPreRequest.preReqCode)
                .orderBy(qPreRequest.preReqCode.desc())
                .fetch();

        long total = queryFactory
                .select(qPreRequest, qRequest.count(),
                        Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} IS NOT NULL THEN 1 ELSE NULL END)",
                                qRequest1.releaseDate))
                .from(qPreRequest)
                .leftJoin(qPreRequest.proPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .leftJoin(qRequest).on(qRequest.preRequest.eq(qPreRequest))
                .leftJoin(qRequest1).on(qRequest1.eq(qRequest))
                .where(builder)
                .groupBy(qPreRequest.preReqCode)
                .orderBy(qPreRequest.preReqCode.desc())
                .fetchCount();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qPreRequest),
                        tuple.get(qRequest.count()),
                        tuple.get(Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} IS NOT NULL THEN 1 ELSE NULL END)",
                                qRequest1.releaseDate))
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }

    @Override
    @Transactional
    public List<Object[]> getStructureStock(Long preReqCode){
        QPreRequest qPreRequest=QPreRequest.preRequest;
        QRequest qRequest=QRequest.request;
        QRequest qRequest1=QRequest.request;
        QMaterial qMaterial=QMaterial.material;
        QProduct qProduct=QProduct.product;
        QStructure qStructure=QStructure.structure;
        QShipment qShipment=QShipment.shipment;
        QContract qContract=QContract.contract;

        BooleanBuilder builder=new BooleanBuilder();

        builder.and( qPreRequest.preReqCode.eq(preReqCode) );

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
                .select(qRequest1.requestNum.sum()).distinct()
                .from(qRequest1)
                .where(qRequest1.material.eq(qMaterial)
                        .and(qRequest1.releaseDate.isNotNull()));

        List<Tuple> tupleList = queryFactory
                .select(qPreRequest, qStructure, qRequest, subQueryShipment, subQueryRequest)
                .from(qPreRequest)
                .leftJoin(qPreRequest.proPlan.product, qProduct)
                .leftJoin(qRequest).on(qRequest.preRequest.eq(qPreRequest))
                .leftJoin(qRequest.material, qMaterial)
                .leftJoin(qStructure).on(qStructure.product.eq(qProduct).and(qStructure.material.eq(qMaterial)))
                .where(builder)
                .groupBy(qRequest.requstCode)
                .orderBy(qStructure.sno.desc())
                .fetch();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qPreRequest),
                        tuple.get(qStructure),
                        tuple.get(qRequest),
                        tuple.get(subQueryShipment),
                        tuple.get(subQueryRequest)
                })
                .collect(Collectors.toList());

        return objectList;
    }
}
