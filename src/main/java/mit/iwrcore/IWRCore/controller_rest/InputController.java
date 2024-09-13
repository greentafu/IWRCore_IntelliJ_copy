package mit.iwrcore.IWRCore.controller_rest;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.PartnerDTO;
import mit.iwrcore.IWRCore.security.service.MemberService;
import mit.iwrcore.IWRCore.security.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/input")
@Log4j2
public class InputController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private PartnerService partnerService;

    @GetMapping("/id")
    public Long id(@RequestParam(required = false) Long mno, @RequestParam(required = false) Long pno, @RequestParam(required = false) String id){
        MemberDTO memberDTO=null;
        PartnerDTO partnerDTO=null;
        MemberDTO idMember=null;
        PartnerDTO idPartner=null;

        if(mno!=null) memberDTO=memberService.findMemberDto(mno, null);
        if(pno!=null) partnerDTO=partnerService.findPartnerDto(pno, null, null);
        if(id!=null){
            idMember=memberService.findMemberDto(null, id);
            idPartner=partnerService.findPartnerDto(null, id, null);
        }

        Long result=0L;

        if(memberDTO==null && partnerDTO==null){
            if(idMember==null && idPartner==null) result=0L;
            else result=1L;
        }else if(memberDTO!=null){
            if(idMember==null && idPartner==null) result=0L;
            else if(idMember!=null){
                if(memberDTO.getId()==idMember.getId()) result=2L; // 자기자신
                else result=1L;
            } else if (idPartner!=null) result=1L;
        }else if(partnerDTO!=null){
            if(idMember==null && idPartner==null) result=0L;
            else if(idMember!=null) result=1L;
            else if(idPartner!=null){
                if(partnerDTO.getId()==idPartner.getId()) result=2L;
                else result=1L;
            }
        }

        return result;
    }

    @GetMapping("/regNum")
    public Long regNum(@RequestParam(required = false) Long pno, @RequestParam(required = false) String registerNum){
        PartnerDTO partnerDTOPno=null;
        PartnerDTO partnerDTOReg=null;
        if(pno!=null){ partnerDTOPno=partnerService.findPartnerDto(pno, null, null); }
        if(registerNum!=null){ partnerDTOReg=partnerService.findPartnerDto(null, null, registerNum); }

        Long result=0L;

        if(partnerDTOPno==null){
            if(partnerDTOReg==null) result=0L;
            else result=1L;
        }else {
            if(partnerDTOReg==null) result=0L;
            else if(partnerDTOPno.getPno().equals(partnerDTOReg.getPno())) result=2L;
            else result=1L;
        }

        return result;
    }
}
