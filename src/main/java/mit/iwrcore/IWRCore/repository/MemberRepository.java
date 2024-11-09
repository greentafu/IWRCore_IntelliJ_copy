package mit.iwrcore.IWRCore.repository;

import mit.iwrcore.IWRCore.entity.Member;
import mit.iwrcore.IWRCore.repositoryDSL.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface  MemberRepository extends JpaRepository<Member, String>, QuerydslPredicateExecutor<Member>, MemberRepositoryCustom {
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.id =:id")
    Optional<Member> findByID(String id);

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.mno =:mno or m.id=:id")
    Member findMember(Long mno, String id);

}
