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

import java.util.ArrayList;
import java.util.List;

public class MaterialRepositoryImpl implements MaterialRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    MaterialRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Material> findMaterialByCustomQuery(PageRequestDTO requestDTO){
        QMaterial qMaterial=QMaterial.material;
        QMaterL qMaterL=QMaterL.materL;
        QMaterM qMaterM=QMaterM.materM;
        QMaterS qMaterS=QMaterS.materS;

        QBox qBox=QBox.box;

        QProduct qProduct=QProduct.product;
        QProL qProL=QProL.proL;
        QProM qProM=QProM.proM;
        QProS qProS=QProS.proS;

        QStructure qStructure=QStructure.structure;

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
        if (requestDTO.getBox()!=null) { builder.and(qMaterial.box.boxCode.eq(requestDTO.getBox())); }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Material> materialList = queryFactory
                .selectDistinct(qMaterial)
                .from(qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qMaterial.box, qBox)
                .leftJoin(qMaterial.structures, qStructure)
                .leftJoin(qStructure.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qMaterial.materCode.desc())
                .fetch();

        long total = queryFactory
                .selectFrom(qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qMaterial.box, qBox)
                .leftJoin(qMaterial.structures, qStructure)
                .leftJoin(qStructure.product, qProduct)
                .leftJoin(qProduct.proS, qProS)
                .leftJoin(qProS.proM, qProM)
                .leftJoin(qProM.proL, qProL)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(materialList, pageable, total);
    }

    @Override
    public Page<Material> findMaterialByCustomQuery2(PageRequestDTO requestDTO){
        QMaterial qMaterial=QMaterial.material;
        QMaterL qMaterL=QMaterL.materL;
        QMaterM qMaterM=QMaterM.materM;
        QMaterS qMaterS=QMaterS.materS;

        QBox qBox=QBox.box;

        BooleanBuilder builder=new BooleanBuilder();

        if (requestDTO.getMaterL() != null) { builder.and(qMaterS.materM.materL.materLcode.eq(requestDTO.getMaterL())); }
        if (requestDTO.getMaterM() != null) { builder.and(qMaterS.materM.materMcode.eq(requestDTO.getMaterM())); }
        if (requestDTO.getMaterS() != null) { builder.and(qMaterS.materScode.eq(requestDTO.getMaterS())); }
        if (requestDTO.getMaterialSearch()!=null){
            builder.and(qMaterial.name.containsIgnoreCase(requestDTO.getMaterialSearch())
                    .or(qMaterial.standard.containsIgnoreCase(requestDTO.getMaterialSearch()))
                    .or(qMaterial.unit.containsIgnoreCase(requestDTO.getMaterialSearch())));
        }
        if (requestDTO.getBox()!=null) { builder.and(qMaterial.box.boxCode.eq(requestDTO.getBox())); }

        if (requestDTO.getMaterials()!=null) { builder.and(qMaterial.materCode.notIn(requestDTO.getMaterials())); }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Material> materialList = queryFactory
                .selectDistinct(qMaterial)
                .from(qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qMaterial.box, qBox)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qMaterial.materCode.desc())
                .fetch();

        long total = queryFactory
                .selectFrom(qMaterial)
                .leftJoin(qMaterial.materS, qMaterS)
                .leftJoin(qMaterS.materM, qMaterM)
                .leftJoin(qMaterM.materL, qMaterL)
                .leftJoin(qMaterial.box, qBox)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(materialList, pageable, total);
    }
}
