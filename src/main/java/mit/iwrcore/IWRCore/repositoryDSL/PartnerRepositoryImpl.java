package mit.iwrcore.IWRCore.repositoryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
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
}
