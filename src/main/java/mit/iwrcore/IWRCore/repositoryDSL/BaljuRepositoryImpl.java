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

public class BaljuRepositoryImpl implements BaljuRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    BaljuRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional
    public Page<Object[]> findBaljuByCustomQuery(PageRequestDTO2 requestDTO){
        QJodalPlan qJodalPlan=QJodalPlan.jodalPlan;
        QContract qContract=QContract.contract;
        QProPlan qProPlan=QProPlan.proPlan;
        QBalju qBalju=QBalju.balju;

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

        if (requestDTO.getBaljuProgress2()!=null){
            if(requestDTO.getBaljuProgress2()==0) { builder.and(qBalju.isNull()); }
            if(requestDTO.getBaljuProgress2()==1) { builder.and(qBalju.isNotNull()); }
        }

        builder.and(qContract.isNotNull());

        Pageable pageable= PageRequest.of(requestDTO.getPage2()-1, requestDTO.getSize2());
        List<Tuple> tupleList = queryFactory
                .select(qContract, qBalju)
                .from(qContract)
                .leftJoin(qContract.baljus, qBalju)
                .leftJoin(qContract.jodalPlan, qJodalPlan)
                .leftJoin(qJodalPlan.proPlan, qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qContract.partner, qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qContract.conNo.desc())
                .fetch();

        long total = queryFactory
                .select(qContract, qBalju)
                .from(qContract)
                .leftJoin(qContract.baljus, qBalju)
                .leftJoin(qContract.jodalPlan, qJodalPlan)
                .leftJoin(qJodalPlan.proPlan, qProPlan)
                .leftJoin(qProPlan.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .leftJoin(qJodalPlan.material, qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qContract.partner, qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .where(builder)
                .fetchCount();

        List<Object[]> objectList = tupleList.stream()
                .map(tuple -> new Object[]{
                        tuple.get(qContract),
                        tuple.get(qBalju)
                })
                .collect(Collectors.toList());

        return new PageImpl<>(objectList, pageable, total);
    }
}
