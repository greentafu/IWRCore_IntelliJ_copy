package mit.iwrcore.IWRCore.repositoryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class LocationRepositoryImpl implements LocationRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    LocationRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Location> findLocationByCustomQuery(PageRequestDTO2 requestDTO){
        QLocation qLocation=QLocation.location1;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getLoc() != null) { builder.and(qLocation.location.contains(requestDTO.getLoc())); }

        Pageable pageable= PageRequest.of(requestDTO.getPage2()-1, requestDTO.getSize2());
        List<Location> locationlList = queryFactory
                .select(qLocation)
                .from(qLocation)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qLocation.useTime.desc())
                .fetch();

        long total = queryFactory
                .select(qLocation)
                .from(qLocation)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(locationlList, pageable, total);
    }
}
