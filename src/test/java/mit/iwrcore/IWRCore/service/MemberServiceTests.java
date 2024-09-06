package mit.iwrcore.IWRCore.service;

import mit.iwrcore.IWRCore.entity.Member;
import mit.iwrcore.IWRCore.repository.MemberRepository;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MemberServiceTests {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 직원 찾기
    @Test
    @Transactional
    @Commit
    public void findMember(){
//        System.out.println(memberService.findMember(1L, null));
//        System.out.println(memberService.findMember(null, "mate1_1234"));
//        System.out.println(memberService.findMemberEntity(1L, null));
//        System.out.println(memberService.findMemberDto(1L, null));
        System.out.println(memberRepository.findAll().size());
    }
    // 직원 리스트
    @Test
    @Transactional
    @Commit
    public void memberList(){

    }
    // 직원 삭제
    @Test
    @Transactional
    @Commit
    public void deleteMember(){
        memberService.deleteMember(44L);
    }
    // 직원 검색
    @Test
    @Transactional
    @Commit
    public void searchMember(){
        PageRequestDTO requestDTO=PageRequestDTO.builder().size(2).page(1).build();
        System.out.println(memberService.findMemberList(requestDTO));
    }
}
