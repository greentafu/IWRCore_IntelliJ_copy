package mit.iwrcore.IWRCore.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.BoxRepository;
import mit.iwrcore.IWRCore.repository.Category.Mater.MaterSRepository;
import mit.iwrcore.IWRCore.repository.Category.Part.PartSCodeRepository;
import mit.iwrcore.IWRCore.repository.Category.Pro.ProSCodeRepository;
import mit.iwrcore.IWRCore.repository.LineRepository;
import mit.iwrcore.IWRCore.repository.MemberRepository;
import mit.iwrcore.IWRCore.repository.PartnerRepository;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterCodeListDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterSDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProSDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartCodeListDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartSDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProCodeListDTO;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthPartnerDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class LoginController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final PartCodeService partCodeService;
    private final MaterService materService;
    private final ProCodeService proCodeService;
    private final ProductService productService;
    private final JodalPlanService jodalPlanService;
    private final MemberRepository memberRepository;
    private final BoxRepository boxRepository;
    private final RequestService requestService;
    private final ShipmentService shipmentService;
    private final PartnerRepository partnerRepository;
    private final BaljuService baljuService;
    private final LineRepository lineRepository;

    private final PartSCodeRepository partSCodeRepository;
    private final MaterSRepository materSRepository;
    private final ProSCodeRepository proSCodeRepository;

    @GetMapping("/login")
    @Transactional
    public void login(){
        // 생산라인
        if(lineRepository.findAll().size()==0){
            Line line0=Line.builder().lineName("A").build();
            Line line1=Line.builder().lineName("B").build();
            Line line2=Line.builder().lineName("C").build();
            lineRepository.save(line0);
            lineRepository.save(line1);
            lineRepository.save(line2);
        }
        // 창고
        if(boxRepository.findAll().size()==0){
            Box box0=Box.builder().boxName("미정").boxCode(1L).build();
            Box box1=Box.builder().boxName("A창고").boxCode(2L).build();
            Box box2=Box.builder().boxName("B창고").boxCode(3L).build();
            Box box3=Box.builder().boxName("C창고").boxCode(4L).build();
            boxRepository.save(box0);
            boxRepository.save(box1);
            boxRepository.save(box2);
            boxRepository.save(box3);
        }
        // 관리자
        if(memberRepository.findAll().size()==0){
            Member member = Member.builder()
                    .mno(1L)
                    .name("관리자")
                    .id("manager")
                    .pw("1111")
                    .password(passwordEncoder.encode("1111"))
                    .phonenumber("000-0000-0000")
                    .department("자재부서")
                    .autoJodalChasu(0L)
                    .autoBaljuChasu(0L)
                    .autoGumsuChasu(0L)
                    .build();
            member.changeMemberRole(MemberRole.MANAGER);
            memberRepository.save(member);
        }
        // 제품코드
        if(proSCodeRepository.findAll().size()==0){
            ProLDTO proLDTO=ProLDTO.builder().Lname("미정").build();
            ProLDTO savedL=proCodeService.insertProL(proLDTO);
            ProMDTO proMDTO=ProMDTO.builder().Mname("미정").proLDTO(savedL).build();
            ProMDTO savedM=proCodeService.insertProM(proMDTO);
            ProSDTO proSDTO=ProSDTO.builder().Sname("미정").proMDTO(savedM).build();
            ProSDTO savedS=proCodeService.insertProS(proSDTO);
        }
        // 자재코드
        if(materSRepository.findAll().size()==0){
            MaterLDTO materLDTO=MaterLDTO.builder().lname("미정").build();
            MaterLDTO savedL=materService.insertML(materLDTO);
            MaterMDTO materMDTO=MaterMDTO.builder().Mname("미정").materLDTO(savedL).build();
            MaterMDTO savedM=materService.insertMM(materMDTO);
            MaterSDTO materSDTO=MaterSDTO.builder().Sname("미정").materMDTO(savedM).build();
            MaterSDTO savedS=materService.insertMS(materSDTO);
        }
        // 회사코드
        if(partSCodeRepository.findAll().size()==0){
            PartLDTO partLDTO=PartLDTO.builder().Lname("미정").build();
            PartLDTO savedL=partCodeService.insertPartL(partLDTO);
            PartMDTO partMDTO=PartMDTO.builder().Mname("미정").partLDTO(savedL).build();
            PartMDTO savedM=partCodeService.insertPartM(partMDTO);
            PartSDTO partSDTO=PartSDTO.builder().Sname("미정").partMDTO(savedM).build();
            PartSDTO savedS=partCodeService.insertPartS(partSDTO);

            PartLDTO partLDTO2=PartLDTO.builder().Lname("소속회사").build();
            PartLDTO savedL2=partCodeService.insertPartL(partLDTO2);
            PartMDTO partMDTO2=PartMDTO.builder().Mname("소속회사").partLDTO(savedL2).build();
            PartMDTO savedM2=partCodeService.insertPartM(partMDTO2);
            PartSDTO partSDTO2=PartSDTO.builder().Sname("소속회사").partMDTO(savedM2).build();
            PartSDTO savedS2=partCodeService.insertPartS(partSDTO2);

            Partner partner=Partner.builder()
                    .name("소속회사")
                    .registrationNumber("000-00-00000")
                    .location("경기도 수원시")
                    .type("제조업")
                    .sector("금속사출부품")
                    .ceo("ceo")
                    .telNumber("000-0000-0000")
                    .faxNumber("000-0000-0001")
                    .email("company@mail.com")
                    .contacter("담당자")
                    .contacterNumber("000-0000-0000")
                    .contacterEmail("damdang@mail.com")
                    .partS(partCodeService.partSdtoToEntity(savedS2))
                    .id("company")
                    .pw("1111")
                    .password(passwordEncoder.encode("1111"))
                    .build();
            partner.setPartnerRole(MemberRole.PARTNER);
            partnerRepository.save(partner);
        }
    }
    @GetMapping("/checkrole")
    public String checkrole(@AuthenticationPrincipal AuthMemberDTO authMember, @AuthenticationPrincipal AuthPartnerDTO authPartner){
        if(authMember!=null) return "redirect:main";
        else if(authPartner!=null) return "redirect:/partner/main";
        else return "redirect:/login?error";
    }
    @GetMapping("/main")
    public void main(PageRequestDTO requestDTO, Model model){
        model.addAttribute("newProductCount", productService.newProductCount());
        model.addAttribute("newNoneChasu", jodalPlanService.newNoneJodalChasuCount());
        model.addAttribute("newShipment", shipmentService.mainShipNum());
        model.addAttribute("newRequest", requestService.mainRequestCount());

        model.addAttribute("baljuProductList", baljuService.baljuProduct());
        model.addAttribute("requestProductList", productService.getRequestProduct());

        model.addAttribute("shipment_list", shipmentService.mainShipment(requestDTO));
    }
    @GetMapping("/category")
    public void category(Model model){
        PartCodeListDTO lists=partCodeService.findListPartAll(null, null,null, 0L);
        model.addAttribute("partCodeList", lists);
        MaterCodeListDTO lists2=materService.findListMaterAll(null, null, null, 0L);
        model.addAttribute("materCodeList", lists2);
        ProCodeListDTO list3=proCodeService.findListProAll(null, null, null, 0L);
        model.addAttribute("proCodeList", list3);
    }

}


