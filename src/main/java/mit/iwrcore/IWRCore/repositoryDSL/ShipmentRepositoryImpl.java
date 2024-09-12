package mit.iwrcore.IWRCore.repositoryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Tuple> tupleList = queryFactory
                .select(qShipment, qGumsu, qShipment.shipNum.sum(), qReturns.reNO)
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
                .select(qShipment, qGumsu, qShipment.shipNum.sum(), qReturns.reNO)
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
                        tuple.get(qShipment.shipNum.sum()),
                        tuple.get(qReturns.reNO)
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }
}
