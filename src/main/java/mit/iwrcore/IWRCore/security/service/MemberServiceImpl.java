package mit.iwrcore.IWRCore.security.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.Member;
import mit.iwrcore.IWRCore.entity.MemberRole;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.entity.QMember;
import mit.iwrcore.IWRCore.repository.MemberRepository;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.security.dto.ProplanDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    // 직원찾기
    @Override
    public Member findMemberEntity(Long mno, String id) {
        return memberRepository.findMember(mno, id);
    }
    @Override
    public MemberDTO findMemberDto(Long mno, String id) {
        return memberTodto(memberRepository.findMember(mno, id));
    }

    // 직원 리스트
    @Override
    public PageResultDTO<MemberDTO, Member> findMemberList(PageRequestDTO requestDTO) {
        Page<Member> entityList=memberRepository.findMemberByCustomQuery(requestDTO);

        Function<Member, MemberDTO> fn = (entity -> memberTodto(entity));
        PageResultDTO<MemberDTO, Member> resultDTO = new PageResultDTO<>(entityList, fn);

        resultDTO.setDepartment(requestDTO.getDepartment());
        resultDTO.setRole(requestDTO.getRole());
        resultDTO.setMemberSearch(requestDTO.getMemberSearch());

        return resultDTO;
    }
    // 직원 삽입(아이디 중복의 경우 0, 성공시 1)
    @Override
    public Integer insertMember(MemberDTO dto, Long role) {
        if(dto.getId()!=null){
            if(findMemberEntity(null, dto.getId())!=null)
                return 0;
            else{
                Member member=memberdtoToEntity(dto);
                if(role==0) member.changeMemberRole(MemberRole.MANAGER);
                if(role==1) member.changeMemberRole(MemberRole.MATERIAL_TEAM);
                if(role==2) member.changeMemberRole(MemberRole.DEV_PROD_TEAM);
                memberRepository.save(member);
                return 1;
            }
        }
        else{
            Member member=memberdtoToEntity(dto);
            if(role==0) member.changeMemberRole(MemberRole.MANAGER);
            if(role==1) member.changeMemberRole(MemberRole.MATERIAL_TEAM);
            if(role==2) member.changeMemberRole(MemberRole.DEV_PROD_TEAM);
            memberRepository.save(member);
            return 1;
        }
    }
    // 직원 삭제
    @Override
    public void deleteMember(Long mno) {
        Member member=findMemberEntity(mno, "");
        memberRepository.delete(member);
    }
}
