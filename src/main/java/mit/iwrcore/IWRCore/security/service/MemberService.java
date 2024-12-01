package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Member;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;

public interface MemberService {

    // 직원 찾기
    Member findMemberEntity(Long mno, String id);
    MemberDTO findMemberDto(Long mno, String id);
    // 직원 목록
    PageResultDTO<MemberDTO, Member> findMemberList(PageRequestDTO requestDTO);
    // 직원 삽입, 수정
    Integer insertMember(MemberDTO dto, Long role);
    MemberDTO updateMemberAuto(Long mno, Long jCheck, Long bCheck, Long gCheck);
    // 직원 삭제
    void deleteMember(Long mno);

    default Member memberdtoToEntity(MemberDTO dto){
        return Member.builder()
                .mno(dto.getMno())
                .department(dto.getDepartment())
                .id(dto.getId())
                .name(dto.getName())
                .phonenumber(dto.getPhonenumber())
                .pw(dto.getPw())
                .password(dto.getPassword())
                .autoJodalChasu(dto.getAutoJodalChasu())
                .autoBaljuChasu(dto.getAutoBaljuChasu())
                .autoGumsuChasu(dto.getAutoGumsuChasu())
                .roleSet(dto.getRoleSet())
                .build();
    }
    default MemberDTO memberTodto(Member entity){
        return MemberDTO.builder()
                .mno(entity.getMno())
                .department(entity.getDepartment())
                .id(entity.getId())
                .name(entity.getName())
                .phonenumber(entity.getPhonenumber())
                .pw(entity.getPw())
                .password(entity.getPassword())
                .autoJodalChasu(entity.getAutoJodalChasu())
                .autoBaljuChasu(entity.getAutoBaljuChasu())
                .autoGumsuChasu(entity.getAutoGumsuChasu())
                .roleSet(entity.getRoleSet())
                .build();
    }
}
