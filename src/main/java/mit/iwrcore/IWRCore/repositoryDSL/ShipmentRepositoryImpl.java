package mit.iwrcore.IWRCore.repositoryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
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

public class ShipmentRepositoryImpl implements ShipmentRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    ShipmentRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional
    public Page<Object[]> findShipmentByCustomQuery(PageRequestDTO requestDTO){
        QJodalPlan qJodalPlan=QJodalPlan.jodalPlan;
        QContract qContract=QContract.contract;
        QProPlan qProPlan=QProPlan.proPlan;
        QBalju qBalju=QBalju.balju;
        QGumsu qGumsu=QGumsu.gumsu;
        QShipment qShipment=QShipment.shipment;
        QReturns qReturns=QReturns.returns;

        QMaterial qMaterial=QMaterial.material;
        QMaterL qMaterL=QMaterL.materL;
        QMaterM qMaterM=QMaterM.materM;
        QMaterS qMaterS=QMaterS.materS;

        QProduct qProduct=QProduct.product;
        QProL qProL=QProL.proL;
        QProM qProM=QProM.proM;
        QProS qProS=QProS.proS;

        QPartner qPartner=QPartner.partner;
        QPartL qPartL=QPartL.partL;
        QPartM qPartM=QPartM.partM;
        QPartS qPartS=QPartS.partS;

        QShipment qSubShipment=QShipment.shipment;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getPartL() != null) { builder.and(qPartS.partM.partL.partLcode.eq(requestDTO.getPartL())); }
        if (requestDTO.getPartM() != null) { builder.and(qPartS.partM.partMcode.eq(requestDTO.getPartM())); }
        if (requestDTO.getPartS() != null) { builder.and(qPartS.partScode.eq(requestDTO.getPartS())); }
        if (requestDTO.getPartnerSearch()!=null){
            builder.and(qPartner.name.containsIgnoreCase(requestDTO.getPartnerSearch())
                    .or(qPartner.registrationNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.telNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.contacter.containsIgnoreCase(requestDTO.getPartnerSearch())));
        }

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

        if (requestDTO.getBaljuFin() != null ) { builder.and(qBalju.finCheck.eq(requestDTO.getBaljuFin())); }
        if (requestDTO.getReceiveReturn() != null){
            if(requestDTO.getReceiveReturn()==0) { builder.and(qShipment.receipt.isNull()); }
            if(requestDTO.getReceiveReturn()==1) { builder.and(qShipment.receipt.isNotNull()).and(qShipment.receiveCheck.eq(0L)).and(qShipment.returns.isNull()); }
            if(requestDTO.getReceiveReturn()==2) { builder.and(qShipment.receipt.isNotNull()).and(qShipment.receiveCheck.eq(1L)).and(qShipment.returns.isNull()); }
            if(requestDTO.getReceiveReturn()==3) { builder.and(qShipment.receipt.isNotNull()).and(qShipment.receiveCheck.eq(0L)).and(qShipment.returns.isNotNull()); }
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Tuple> tupleList = queryFactory
                .select(qShipment, qGumsu,
                        JPAExpressions
                                .select(qShipment.shipNum.sum())
                                .from(qShipment)
                                .where(qShipment.receiveCheck.eq(1L), qShipment.balju.baljuNo.eq(qBalju.baljuNo))
                                .groupBy(qShipment.balju.baljuNo)
                        , qReturns.reNO)
                .from(qShipment)
                .leftJoin(qShipment.returns, qReturns)
                .leftJoin(qShipment.balju, qBalju)
                .leftJoin(qBalju.gumsu, qGumsu)
                .leftJoin(qBalju.contract, qContract)
                .leftJoin(qContract.partner, qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract.jodalPlan, qJodalPlan)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qJodalPlan.proPlan, qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .where(builder)
                .groupBy(qShipment.shipNO)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qShipment.shipNO.desc())
                .fetch();

        long total = queryFactory
                .select(qShipment, qGumsu,
                        JPAExpressions
                                .select(qShipment.shipNum.sum())
                                .from(qShipment)
                                .where(qShipment.receiveCheck.eq(1L), qShipment.balju.baljuNo.eq(qBalju.baljuNo))
                                .groupBy(qShipment.balju.baljuNo)
                        , qReturns.reNO)
                .from(qShipment)
                .leftJoin(qShipment.returns, qReturns)
                .leftJoin(qShipment.balju, qBalju)
                .leftJoin(qBalju.gumsu, qGumsu)
                .leftJoin(qBalju.contract, qContract)
                .leftJoin(qContract.partner, qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract.jodalPlan, qJodalPlan)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qJodalPlan.proPlan, qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .where(builder)
                .groupBy(qShipment.shipNO)
                .fetchCount();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qShipment),
                        tuple.get(qGumsu),
                        tuple.get(JPAExpressions
                                .select(qShipment.shipNum.sum())
                                .from(qShipment)
                                .where(qShipment.receiveCheck.eq(1L), qShipment.balju.baljuNo.eq(qBalju.baljuNo))
                                .groupBy(qShipment.balju.baljuNo)),
                        tuple.get(qReturns.reNO)
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }

    @Override
    @Transactional
    public Page<Object[]> findShipmentByCustomQuery2(PageRequestDTO requestDTO){
        QJodalPlan qJodalPlan=QJodalPlan.jodalPlan;
        QContract qContract=QContract.contract;
        QProPlan qProPlan=QProPlan.proPlan;
        QBalju qBalju=QBalju.balju;
        QGumsu qGumsu=QGumsu.gumsu;
        QShipment qShipment=QShipment.shipment;
        QReturns qReturns=QReturns.returns;

        QMaterial qMaterial=QMaterial.material;
        QMaterL qMaterL=QMaterL.materL;
        QMaterM qMaterM=QMaterM.materM;
        QMaterS qMaterS=QMaterS.materS;

        QProduct qProduct=QProduct.product;
        QProL qProL=QProL.proL;
        QProM qProM=QProM.proM;
        QProS qProS=QProS.proS;

        QPartner qPartner=QPartner.partner;
        QPartL qPartL=QPartL.partL;
        QPartM qPartM=QPartM.partM;
        QPartS qPartS=QPartS.partS;

        BooleanBuilder builder=new BooleanBuilder();

        builder.and(qShipment.receipt.isNull());
        if(requestDTO.getBaljuProductName()!=null){ builder.and(qProduct.manuCode.eq(requestDTO.getBaljuProductName())); }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Tuple> tupleList = queryFactory
                .select(qShipment, qGumsu,
                        JPAExpressions
                                .select(qShipment.shipNum.sum())
                                .from(qShipment)
                                .where(qShipment.receiveCheck.eq(1L), qShipment.balju.baljuNo.eq(qBalju.baljuNo))
                                .groupBy(qShipment.balju.baljuNo)
                        , qReturns.reNO)
                .from(qShipment)
                .leftJoin(qShipment.returns, qReturns)
                .leftJoin(qShipment.balju, qBalju)
                .leftJoin(qBalju.gumsu, qGumsu)
                .leftJoin(qBalju.contract, qContract)
                .leftJoin(qContract.partner, qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract.jodalPlan, qJodalPlan)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qJodalPlan.proPlan, qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .where(builder)
                .groupBy(qShipment.shipNO)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qShipment.shipNO.desc())
                .fetch();

        long total = queryFactory
                .select(qShipment, qGumsu,
                        JPAExpressions
                                .select(qShipment.shipNum.sum())
                                .from(qShipment)
                                .where(qShipment.receiveCheck.eq(1L), qShipment.balju.baljuNo.eq(qBalju.baljuNo))
                                .groupBy(qShipment.balju.baljuNo)
                        , qReturns.reNO)
                .from(qShipment)
                .leftJoin(qShipment.returns, qReturns)
                .leftJoin(qShipment.balju, qBalju)
                .leftJoin(qBalju.gumsu, qGumsu)
                .leftJoin(qBalju.contract, qContract)
                .leftJoin(qContract.partner, qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract.jodalPlan, qJodalPlan)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qJodalPlan.proPlan, qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .where(builder)
                .groupBy(qShipment.shipNO)
                .fetchCount();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qShipment),
                        tuple.get(qGumsu),
                        tuple.get(JPAExpressions
                                .select(qShipment.shipNum.sum())
                                .from(qShipment)
                                .where(qShipment.receiveCheck.eq(1L), qShipment.balju.baljuNo.eq(qBalju.baljuNo))
                                .groupBy(qShipment.balju.baljuNo)),
                        tuple.get(qReturns.reNO)
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }

    @Override
    @Transactional
    public Page<Object[]> findShipmentByCustomQuery3(PageRequestDTO requestDTO){
        QJodalPlan qJodalPlan=QJodalPlan.jodalPlan;
        QContract qContract=QContract.contract;
        QProPlan qProPlan=QProPlan.proPlan;
        QBalju qBalju=QBalju.balju;
        QGumsu qGumsu=QGumsu.gumsu;
        QShipment qShipment=QShipment.shipment;
        QReturns qReturns=QReturns.returns;

        QMaterial qMaterial=QMaterial.material;
        QMaterL qMaterL=QMaterL.materL;
        QMaterM qMaterM=QMaterM.materM;
        QMaterS qMaterS=QMaterS.materS;

        QProduct qProduct=QProduct.product;
        QProL qProL=QProL.proL;
        QProM qProM=QProM.proM;
        QProS qProS=QProS.proS;

        QPartner qPartner=QPartner.partner;
        QPartL qPartL=QPartL.partL;
        QPartM qPartM=QPartM.partM;
        QPartS qPartS=QPartS.partS;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getPartL() != null) { builder.and(qPartS.partM.partL.partLcode.eq(requestDTO.getPartL())); }
        if (requestDTO.getPartM() != null) { builder.and(qPartS.partM.partMcode.eq(requestDTO.getPartM())); }
        if (requestDTO.getPartS() != null) { builder.and(qPartS.partScode.eq(requestDTO.getPartS())); }
        if (requestDTO.getPartnerSearch()!=null){
            builder.and(qPartner.name.containsIgnoreCase(requestDTO.getPartnerSearch())
                    .or(qPartner.registrationNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.telNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.contacter.containsIgnoreCase(requestDTO.getPartnerSearch())));
        }

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

        builder.and(qShipment.receiveCheck.eq(1L));
        builder.and(qShipment.invoice.isNull());

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Tuple> tupleList = queryFactory
                .select(qShipment, qContract)
                .from(qShipment)
                .leftJoin(qShipment.returns, qReturns)
                .leftJoin(qShipment.balju, qBalju)
                .leftJoin(qBalju.gumsu, qGumsu)
                .leftJoin(qBalju.contract, qContract)
                .leftJoin(qContract.partner, qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract.jodalPlan, qJodalPlan)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qJodalPlan.proPlan, qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .where(builder)
                .groupBy(qShipment.shipNO)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qShipment.shipNO.desc())
                .fetch();

        long total = queryFactory
                .select(qShipment, qContract)
                .from(qShipment)
                .leftJoin(qShipment.returns, qReturns)
                .leftJoin(qShipment.balju, qBalju)
                .leftJoin(qBalju.gumsu, qGumsu)
                .leftJoin(qBalju.contract, qContract)
                .leftJoin(qContract.partner, qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract.jodalPlan, qJodalPlan)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qJodalPlan.proPlan, qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .where(builder)
                .groupBy(qShipment.shipNO)
                .fetchCount();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qShipment),
                        tuple.get(qContract)
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }

    @Override
    @Transactional
    public Page<Object[]> findShipmentByCustomQuery4(PageRequestDTO2 requestDTO){
        QJodalPlan qJodalPlan=QJodalPlan.jodalPlan;
        QContract qContract=QContract.contract;
        QProPlan qProPlan=QProPlan.proPlan;
        QBalju qBalju=QBalju.balju;
        QGumsu qGumsu=QGumsu.gumsu;
        QShipment qShipment=QShipment.shipment;
        QReturns qReturns=QReturns.returns;
        QInvoice qInvoice=QInvoice.invoice;

        QMaterial qMaterial=QMaterial.material;
        QMaterL qMaterL=QMaterL.materL;
        QMaterM qMaterM=QMaterM.materM;
        QMaterS qMaterS=QMaterS.materS;

        QProduct qProduct=QProduct.product;
        QProL qProL=QProL.proL;
        QProM qProM=QProM.proM;
        QProS qProS=QProS.proS;

        QPartner qPartner=QPartner.partner;
        QPartL qPartL=QPartL.partL;
        QPartM qPartM=QPartM.partM;
        QPartS qPartS=QPartS.partS;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getPartL2() != null) { builder.and(qPartS.partM.partL.partLcode.eq(requestDTO.getPartL2())); }
        if (requestDTO.getPartM2() != null) { builder.and(qPartS.partM.partMcode.eq(requestDTO.getPartM2())); }
        if (requestDTO.getPartS2() != null) { builder.and(qPartS.partScode.eq(requestDTO.getPartS2())); }
        if (requestDTO.getPartnerSearch2()!=null){
            builder.and(qPartner.name.containsIgnoreCase(requestDTO.getPartnerSearch2())
                    .or(qPartner.registrationNumber.containsIgnoreCase(requestDTO.getPartnerSearch2()))
                    .or(qPartner.telNumber.containsIgnoreCase(requestDTO.getPartnerSearch2()))
                    .or(qPartner.contacter.containsIgnoreCase(requestDTO.getPartnerSearch2())));
        }

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

        Pageable pageable= PageRequest.of(requestDTO.getPage2()-1, requestDTO.getSize2());
        List<Tuple> tupleList = queryFactory
                .select(qInvoice, qPartner, qContract)
                .from(qInvoice)
                .leftJoin(qInvoice.shipments, qShipment)
                .leftJoin(qShipment.returns, qReturns)
                .leftJoin(qShipment.balju, qBalju)
                .leftJoin(qBalju.gumsu, qGumsu)
                .leftJoin(qBalju.contract, qContract)
                .leftJoin(qContract.partner, qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract.jodalPlan, qJodalPlan)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qJodalPlan.proPlan, qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .where(builder)
                .groupBy(qInvoice.tranNO)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qInvoice.tranNO.desc())
                .fetch();

        long total = queryFactory
                .select(qInvoice, qPartner, qContract)
                .from(qInvoice)
                .leftJoin(qInvoice.shipments, qShipment)
                .leftJoin(qShipment.returns, qReturns)
                .leftJoin(qShipment.balju, qBalju)
                .leftJoin(qBalju.gumsu, qGumsu)
                .leftJoin(qBalju.contract, qContract)
                .leftJoin(qContract.partner, qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract.jodalPlan, qJodalPlan)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qJodalPlan.proPlan, qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .where(builder)
                .groupBy(qInvoice.tranNO)
                .fetchCount();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qInvoice),
                        tuple.get(qPartner)
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }

    @Override
    public Page<Shipment> getInvoiceShipment(PageRequestDTO2 requestDTO){
        QShipment qShipment=QShipment.shipment;
        QBalju qBalju=QBalju.balju;
        QContract qContract=QContract.contract;
        QPartner qPartner=QPartner.partner;

        QJodalPlan qJodalPlan=QJodalPlan.jodalPlan;
        QMaterial qMaterial=QMaterial.material;
        QMaterL qMaterL=QMaterL.materL;
        QMaterM qMaterM=QMaterM.materM;
        QMaterS qMaterS=QMaterS.materS;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getMaterL2() != null) { builder.and(qMaterS.materM.materL.materLcode.eq(requestDTO.getMaterL2())); }
        if (requestDTO.getMaterM2() != null) { builder.and(qMaterS.materM.materMcode.eq(requestDTO.getMaterM2())); }
        if (requestDTO.getMaterS2() != null) { builder.and(qMaterS.materScode.eq(requestDTO.getMaterS2())); }
        if (requestDTO.getMaterialSearch2()!=null){
            builder.and(qMaterial.name.containsIgnoreCase(requestDTO.getMaterialSearch2())
                    .or(qMaterial.standard.containsIgnoreCase(requestDTO.getMaterialSearch2()))
                    .or(qMaterial.unit.containsIgnoreCase(requestDTO.getMaterialSearch2())));
        }
        if (requestDTO.getShipments() != null) {
            builder.and(qShipment.shipNO.notIn(requestDTO.getShipments()));
        }
        if (requestDTO.getCode() != null) {
            builder.and(qShipment.invoice.isNull().or(qShipment.invoice.tranNO.eq(requestDTO.getCode())));
        }else{
            builder.and(qShipment.invoice.isNull());
        }

        builder.and(qPartner.pno.eq(requestDTO.getPno2()));
        builder.and(qShipment.receiveCheck.eq(1L));

        Pageable pageable= PageRequest.of(requestDTO.getPage2()-1, requestDTO.getSize2());
        List<Shipment> shipmentList= queryFactory
                .selectFrom(qShipment)
                .leftJoin(qShipment.balju, qBalju)
                .leftJoin(qBalju.contract, qContract)
                .leftJoin(qContract.partner, qPartner)
                .leftJoin(qContract.jodalPlan, qJodalPlan)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qShipment.shipNO.desc())
                .fetch();
        long total=queryFactory
                .selectFrom(qShipment)
                .leftJoin(qShipment.balju, qBalju)
                .leftJoin(qBalju.contract, qContract)
                .leftJoin(qContract.partner, qPartner)
                .leftJoin(qContract.jodalPlan, qJodalPlan)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(shipmentList, pageable, total);
    }
}
