package mit.iwrcore.IWRCore.repositoryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import mit.iwrcore.IWRCore.entity.Member;
import mit.iwrcore.IWRCore.entity.MemberRole;
import mit.iwrcore.IWRCore.entity.QMember;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired MemberRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Member> findMemberByCustomQuery(PageRequestDTO requestDTO){
        QMember qMember=QMember.member;
        BooleanBuilder builder=new BooleanBuilder();

        if(requestDTO.getDepartment()!=null){ builder.and(qMember.department.eq(requestDTO.getDepartment())); }
        if(requestDTO.getRole()!=null){ builder.and(qMember.roleSet.contains(MemberRole.valueOf(requestDTO.getRole()))); }
        if(requestDTO.getMemberSearch()!=null){
            builder.and(qMember.name.containsIgnoreCase(requestDTO.getMemberSearch())
                    .or(qMember.phonenumber.containsIgnoreCase(requestDTO.getMemberSearch()))
                    .or(qMember.id.containsIgnoreCase(requestDTO.getMemberSearch())));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Member> memberList= queryFactory
                .selectFrom(qMember)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qMember.mno.desc())
                .fetch();
        long total=queryFactory.selectFrom(qMember).where(builder).fetchCount();

        return new PageImpl<>(memberList, pageable, total);
    }
}
