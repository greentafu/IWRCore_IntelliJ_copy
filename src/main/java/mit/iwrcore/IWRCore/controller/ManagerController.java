package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterCodeListDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartCodeListDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartSDTO;
import mit.iwrcore.IWRCore.security.dto.PartnerDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProCodeListDTO;
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
    private final MaterService materService;
    private final ProCodeService proCodeService;
    private final MemberService memberService;
    private final PartnerService partnerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list_member")
    public void list_member(@ModelAttribute PageRequestDTO pageRequestDTO, Model model){
        if(pageRequestDTO.getDepartment()==""){ pageRequestDTO.setDepartment(null); }
        if(pageRequestDTO.getRole()==""){ pageRequestDTO.setRole(null); }
        if(pageRequestDTO.getMemberSearch()==""){ pageRequestDTO.setMemberSearch(null); }
        model.addAttribute("member_list", memberService.findMemberList(pageRequestDTO));
    }
    @GetMapping("/add_member")
    public void add_member(){

    }
    @GetMapping("/modify_member")
    public void modify_member(Long mno, Model model){
        model.addAttribute("member", memberService.findMemberDto(mno, null));
    }
    @GetMapping("/list_partner")
    public void list_partner(@ModelAttribute PageRequestDTO pageRequestDTO, Model model){
        if(pageRequestDTO.getPartL()+""==""){ pageRequestDTO.setPartL(null); }
        if(pageRequestDTO.getPartM()+""==""){ pageRequestDTO.setPartM(null); }
        if(pageRequestDTO.getPartS()+""==""){ pageRequestDTO.setPartS(null); }
        if(pageRequestDTO.getPartnerSearch()==""){ pageRequestDTO.setPartnerSearch(null); }

        model.addAttribute("partner_list",partnerService.findPartnerList(pageRequestDTO));
        model.addAttribute("request", pageRequestDTO);
    }
    @GetMapping("/add_partner")
    public void add_partner(){

    }
    @GetMapping("/modify_partner")
    public void modify_partner(Long pno, Model model){
        model.addAttribute("partner", partnerService.findPartnerDto(pno, null, null));
    }
    @GetMapping("/category")
    public void category(Model model){
        PartCodeListDTO lists=partCodeService.findListPartAll(null, null,null);
        model.addAttribute("partCodeList", lists);
        MaterCodeListDTO lists2=materService.findListMaterAll(null, null, null);
        model.addAttribute("materCodeList", lists2);
        ProCodeListDTO list3=proCodeService.findListProAll(null, null, null);
        model.addAttribute("proCodeList", list3);
    }
    @PostMapping("/save_member")
    public String save_member(@RequestParam(required = false) Long mno, @RequestParam String name, @RequestParam String phonenumber,
                            @RequestParam String department, @RequestParam Long role,
                            @RequestParam(required = false) String id, @RequestParam(required = false) String pw){
        String temp_pw=(pw!=null && pw!="")?pw:"1111";
        String temp_id=null;
        if(id!=null && id!="") temp_id=id;
        else if(mno!=null && (id==null || id=="")){
            String head="";
            switch(department) {
                case "자재부서": head="mate"; break;
                case "개발부서": head="deve"; break;
                case "생산부서": head="prod";
            }
            temp_id=head+mno+"_"+phonenumber.substring(9);
        }

        MemberDTO memberDTO=MemberDTO.builder()
                .mno((mno!=null)?mno:null).name(name).phonenumber(phonenumber).department(department)
                .id(temp_id).pw(temp_pw).password(passwordEncoder.encode(temp_pw))
                .autoJodalChasu(0L).autoBaljuChasu(0L).autoGumsuChasu(0L)
                .build();

        if(mno!=null) {
            MemberDTO exMemberDTO=memberService.findMemberDto(mno, null);
            memberDTO.setAutoJodalChasu(exMemberDTO.getAutoJodalChasu());
            memberDTO.setAutoBaljuChasu(exMemberDTO.getAutoBaljuChasu());
            memberDTO.setAutoGumsuChasu(exMemberDTO.getAutoGumsuChasu());
        }

        memberService.insertMember(memberDTO, role);

        return "redirect:/manager/list_member";
    }
    @GetMapping ("/save_partner")
    public String save_partner(@RequestParam(required = false) Long pno, @RequestParam(required = false) String name, @RequestParam(required = false) String registerNum,
                             @RequestParam(required = false) String ceo, @RequestParam(required = false) String location, @RequestParam(required = false) String type,
                             @RequestParam(required = false) String sector, @RequestParam(required = false) String phonenumber, @RequestParam(required = false) String faxnumber,
                             @RequestParam(required = false) String email, @RequestParam(required = false) Long selectPartS, @RequestParam(required = false) String cname,
                             @RequestParam(required = false) String cnumber, @RequestParam(required = false) String cmail, @RequestParam(required = false) String id,
                             @RequestParam(required = false) String pw){
        Long temp_pno=(pno!=null)?pno:null;
        String temp_pw=(pw!=null && pw!="")?pw:"1111";
        String temp_id=null;
        if(id!=null && id!="") temp_id=id;
        else if(pno!=null && (id==null || id=="")){
            temp_id="partner" + pno + "_" + registerNum.substring(7);
        }

        PartSDTO partSDTO=partCodeService.findPartS(selectPartS);
        PartnerDTO partnerDTO=PartnerDTO.builder()
                .pno(temp_pno).name(name).registrationNumber(registerNum)
                .ceo(ceo).location(location).type(type).sector(sector)
                .telNumber(phonenumber).faxNumber(faxnumber).email(email)
                .partSDTO(partSDTO)
                .contacter(cname).contacterNumber(cnumber).contacterEmail(cmail)
                .id(temp_id).pw(temp_pw).password(passwordEncoder.encode(temp_pw)).build();
        partnerService.insertPartner(partnerDTO);
        return "redirect:/manager/list_partner";
    }
    @GetMapping("/delete_member")
    public String delete_member(@RequestParam(required = false) Long mno){
        memberService.deleteMember(mno);
        return "redirect:/manager/list_member";
    }
    @GetMapping("/delete_partner")
    public String delete_partner(@RequestParam(required = false) Long pno){
        partnerService.deletePartner(pno);
        return "redirect:/manager/list_partner";
    }

}
