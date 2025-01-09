package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
@Log4j2
public class ManagerController {
    private final PartCodeService partCodeService;
    private final MemberService memberService;
    private final PartnerService partnerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list_member")
    public void list_member(){}
    @GetMapping("/add_member")
    public void add_member(){}
    @GetMapping("/modify_member")
    public void modify_member(Long mno, Model model){
        model.addAttribute("member", memberService.findMemberDto(mno, null));
    }
    @GetMapping("/member")
    public void member(Long mno, Model model){
        model.addAttribute("member", memberService.findMemberDto(mno, null));
    }
    @GetMapping("/list_partner")
    public void list_partner(){}
    @GetMapping("/add_partner")
    public void add_partner(){}
    @GetMapping("/modify_partner")
    public void modify_partner(Long pno, Model model){
        model.addAttribute("partner", partnerService.findPartnerDto(pno, null, null));
    }
    @GetMapping("/partner")
    public void partner(Long pno, Model model){
        model.addAttribute("partner", partnerService.findPartnerDto(pno, null, null));
    }
    @GetMapping("/category")
    public void category(){}
}
