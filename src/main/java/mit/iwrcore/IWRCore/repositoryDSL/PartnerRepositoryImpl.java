package mit.iwrcore.IWRCore.repositoryDSL;

import com.querydsl.core.BooleanBuilder;
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

public class PartnerRepositoryImpl implements PartnerRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    public PartnerRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Partner> findPartnerByCustomQuery(PageRequestDTO requestDTO){
        QPartner qPartner=QPartner.partner;
        QPartS qPartS = QPartS.partS;
        QPartM qPartM = QPartM.partM;
        QPartL qPartL = QPartL.partL;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getPartL() != null) {
            builder.and(qPartS.partM.partL.partLcode.eq(requestDTO.getPartL()));
        }
        if (requestDTO.getPartM() != null) {
            builder.and(qPartS.partM.partMcode.eq(requestDTO.getPartM()));
        }
        if (requestDTO.getPartS() != null) {
            builder.and(qPartS.partScode.eq(requestDTO.getPartS()));
        }

        if (requestDTO.getPartnerSearch() != null) {
            builder.and(qPartner.name.containsIgnoreCase(requestDTO.getPartnerSearch())
                    .or(qPartner.registrationNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.telNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.contacter.containsIgnoreCase(requestDTO.getPartnerSearch())));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Partner> partnerList= queryFactory
                .selectFrom(qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPartner.pno.desc())
                .fetch();
        long total=queryFactory
                .selectFrom(qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(partnerList, pageable, total);
    }

    @Override
    public Page<Partner> getBaljuPartner(PageRequestDTO requestDTO){
        QPartner qPartner=QPartner.partner;
        QPartS qPartS = QPartS.partS;
        QPartM qPartM = QPartM.partM;
        QPartL qPartL = QPartL.partL;

        QContract qContract=QContract.contract;
        QBalju qBalju=QBalju.balju;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getPartL() != null) {
            builder.and(qPartS.partM.partL.partLcode.eq(requestDTO.getPartL()));
        }
        if (requestDTO.getPartM() != null) {
            builder.and(qPartS.partM.partMcode.eq(requestDTO.getPartM()));
        }
        if (requestDTO.getPartS() != null) {
            builder.and(qPartS.partScode.eq(requestDTO.getPartS()));
        }

        if (requestDTO.getPartnerSearch() != null) {
            builder.and(qPartner.name.containsIgnoreCase(requestDTO.getPartnerSearch())
                    .or(qPartner.registrationNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.telNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.contacter.containsIgnoreCase(requestDTO.getPartnerSearch())));
        }

        builder.and(qPartner.pno.ne(1L));
        builder.and(qContract.isNotNull());
        builder.and(qBalju.isNull());

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Partner> partnerList= queryFactory
                .selectFrom(qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract).on(qContract.partner.eq(qPartner))
                .leftJoin(qBalju).on(qBalju.contract.eq(qContract))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPartner.pno.desc())
                .fetch();
        long total=queryFactory
                .selectFrom(qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract).on(qContract.partner.eq(qPartner))
                .leftJoin(qBalju).on(qBalju.contract.eq(qContract))
                .where(builder)
                .fetchCount();

        return new PageImpl<>(partnerList, pageable, total);
    }

    @Override
    public Page<Partner> getGumsuPartner(PageRequestDTO requestDTO){
        QPartner qPartner=QPartner.partner;
        QPartS qPartS = QPartS.partS;
        QPartM qPartM = QPartM.partM;
        QPartL qPartL = QPartL.partL;

        QContract qContract=QContract.contract;
        QBalju qBalju=QBalju.balju;
        QGumsu qGumsu=QGumsu.gumsu;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getPartL() != null) {
            builder.and(qPartS.partM.partL.partLcode.eq(requestDTO.getPartL()));
        }
        if (requestDTO.getPartM() != null) {
            builder.and(qPartS.partM.partMcode.eq(requestDTO.getPartM()));
        }
        if (requestDTO.getPartS() != null) {
            builder.and(qPartS.partScode.eq(requestDTO.getPartS()));
        }

        if (requestDTO.getPartnerSearch() != null) {
            builder.and(qPartner.name.containsIgnoreCase(requestDTO.getPartnerSearch())
                    .or(qPartner.registrationNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.telNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.contacter.containsIgnoreCase(requestDTO.getPartnerSearch())));
        }

        builder.and(qPartner.pno.ne(1L));
        builder.and(qContract.isNotNull());
        builder.and(qBalju.isNotNull());
        builder.and(qGumsu.isNull());

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Partner> partnerList= queryFactory
                .selectFrom(qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract).on(qContract.partner.eq(qPartner))
                .leftJoin(qBalju).on(qBalju.contract.eq(qContract))
                .leftJoin(qGumsu).on(qGumsu.balju.eq(qBalju))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPartner.pno.desc())
                .fetch();
        long total=queryFactory
                .selectFrom(qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract).on(qContract.partner.eq(qPartner))
                .leftJoin(qBalju).on(qBalju.contract.eq(qContract))
                .leftJoin(qGumsu).on(qGumsu.balju.eq(qBalju))
                .where(builder)
                .fetchCount();

        return new PageImpl<>(partnerList, pageable, total);
    }

    @Override
    public Page<Partner> getAllPartner(PageRequestDTO requestDTO){
        QPartner qPartner=QPartner.partner;
        QPartS qPartS = QPartS.partS;
        QPartM qPartM = QPartM.partM;
        QPartL qPartL = QPartL.partL;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getPartL() != null) {
            builder.and(qPartS.partM.partL.partLcode.eq(requestDTO.getPartL()));
        }
        if (requestDTO.getPartM() != null) {
            builder.and(qPartS.partM.partMcode.eq(requestDTO.getPartM()));
        }
        if (requestDTO.getPartS() != null) {
            builder.and(qPartS.partScode.eq(requestDTO.getPartS()));
        }

        if (requestDTO.getPartnerSearch() != null) {
            builder.and(qPartner.name.containsIgnoreCase(requestDTO.getPartnerSearch())
                    .or(qPartner.registrationNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.telNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.contacter.containsIgnoreCase(requestDTO.getPartnerSearch())));
        }

        builder.and(qPartner.pno.ne(1L));

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Partner> partnerList= queryFactory
                .selectFrom(qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPartner.pno.desc())
                .fetch();
        long total=queryFactory
                .selectFrom(qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(partnerList, pageable, total);
    }

    @Override
    public Page<Partner> getInvoicePartner(PageRequestDTO requestDTO){
        QPartner qPartner=QPartner.partner;
        QPartS qPartS = QPartS.partS;
        QPartM qPartM = QPartM.partM;
        QPartL qPartL = QPartL.partL;

        QShipment qShipment=QShipment.shipment;
        QBalju qBalju=QBalju.balju;
        QContract qContract=QContract.contract;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getPartL() != null) {
            builder.and(qPartS.partM.partL.partLcode.eq(requestDTO.getPartL()));
        }
        if (requestDTO.getPartM() != null) {
            builder.and(qPartS.partM.partMcode.eq(requestDTO.getPartM()));
        }
        if (requestDTO.getPartS() != null) {
            builder.and(qPartS.partScode.eq(requestDTO.getPartS()));
        }

        if (requestDTO.getPartnerSearch() != null) {
            builder.and(qPartner.name.containsIgnoreCase(requestDTO.getPartnerSearch())
                    .or(qPartner.registrationNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.telNumber.containsIgnoreCase(requestDTO.getPartnerSearch()))
                    .or(qPartner.contacter.containsIgnoreCase(requestDTO.getPartnerSearch())));
        }

        builder.and(qPartner.pno.ne(1L));
        builder.and(qShipment.receiveCheck.eq(1L));
        builder.and(qShipment.invoice.isNull());

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Partner> partnerList= queryFactory
                .selectFrom(qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract).on(qContract.partner.eq(qPartner))
                .leftJoin(qBalju).on(qBalju.contract.eq(qContract))
                .leftJoin(qShipment).on(qShipment.balju.eq(qBalju))
                .where(builder)
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPartner.pno.desc())
                .fetch();
        long total=queryFactory
                .selectFrom(qPartner)
                .leftJoin(qPartner.partS, qPartS)
                .leftJoin(qPartS.partM, qPartM)
                .leftJoin(qPartM.partL, qPartL)
                .leftJoin(qContract).on(qContract.partner.eq(qPartner))
                .leftJoin(qBalju).on(qBalju.contract.eq(qContract))
                .leftJoin(qShipment).on(qShipment.balju.eq(qBalju))
                .where(builder)
                .distinct()
                .fetchCount();

        return new PageImpl<>(partnerList, pageable, total);
    }
}
