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

public class ReturnsRepositoryImpl implements ReturnsRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    ReturnsRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional
    public Page<Returns> partnerReturnsPage(PageRequestDTO requestDTO){
        QReturns qReturns=QReturns.returns;
        QShipment qShipment=QShipment.shipment;
        QBalju qBalju=QBalju.balju;
        QContract qContract=QContract.contract;
        QPartner qPartner=QPartner.partner;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getPno()!=null) { builder.and(qPartner.pno.eq(requestDTO.getPno())); }

        if (requestDTO.getReturnCheck()!=null) { builder.and(qReturns.returnCheck.eq(requestDTO.getReturnCheck()));  }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Returns> returnList = queryFactory
                .select(qReturns)
                .from(qReturns)
                .leftJoin(qReturns.shipment, qShipment)
                .leftJoin(qShipment.balju, qBalju)
                .leftJoin(qBalju.contract, qContract)
                .leftJoin(qContract.partner, qPartner)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qReturns.reNO.desc())
                .fetch();

        long total = queryFactory
                .select(qReturns)
                .from(qReturns)
                .leftJoin(qReturns.shipment, qShipment)
                .leftJoin(qShipment.balju, qBalju)
                .leftJoin(qBalju.contract, qContract)
                .leftJoin(qContract.partner, qPartner)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(returnList, pageable, total);
    }
}
