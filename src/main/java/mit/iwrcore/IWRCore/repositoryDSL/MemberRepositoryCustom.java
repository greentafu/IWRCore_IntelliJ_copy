package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.Member;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface MemberRepositoryCustom {
    Page<Member> findMemberByCustomQuery(PageRequestDTO requestDTO);
}
