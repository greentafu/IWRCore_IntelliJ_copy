package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@Log4j2
public class OrderController {
    private final BaljuService baljuService;
    private final ContractService contractService;
    private final MemberService memberService;
    private final EmergencyService emergencyService;
    private final BaljuChasuService baljuChasuService;

    @GetMapping("/list_order")
    public void list_order(){}
    @GetMapping("/new_order")
    public void new_order(){}
    @GetMapping("/new_order2")
    public void new_order2(@RequestParam(required = false) Long conNo, Model model){
        ContractDTO contractDTO=contractService.getContractById(conNo);
        model.addAttribute("partner", contractDTO.getPartnerDTO());
    }
    @GetMapping("/modify_order")
    public void modify_order2(Long baljuNo, Model model){
        PartnerDTO partnerDTO=baljuService.getBaljuById(baljuNo).getContractDTO().getPartnerDTO();
        model.addAttribute("partner", partnerDTO);
        model.addAttribute("baljuList", baljuChasuService.modifyBalju(partnerDTO.getPno()));
    }
    @GetMapping("/order")
    public void download_order(Long baljuNo, Model model){
        PartnerDTO partnerDTO=baljuService.getBaljuById(baljuNo).getContractDTO().getPartnerDTO();
        model.addAttribute("partner", partnerDTO);
        model.addAttribute("baljuList", baljuChasuService.modifyBalju(partnerDTO.getPno()));
    }




//    @PostMapping ("/delete_order")
//    public String delete_order(@RequestParam(required = false) Long baljuNo){
//        baljuService.deleteBalju(baljuNo);
//        return "redirect:/order/list_order";
//    }
    @PostMapping("/urgent")
    public void urgent(){

    }
    @PostMapping("/createEmergency")
    public ResponseEntity<Void> createEmergency(@RequestBody EmergencyDTO emergencyDTO) {
        log.info("Received EmergencyDTO: {}", emergencyDTO);

        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO = memberService.findMemberDto(authMemberDTO.getMno(), null);
        Member writer = memberService.memberdtoToEntity(memberDTO);

        try {
            // Emergency 엔티티 생성 및 필수 정보 설정
            emergencyDTO.setMemberDTO(memberDTO);

            // Emergency 엔티티를 서비스 레이어를 통해 저장
            emergencyService.createEmergency(emergencyDTO);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error processing EmergencyDTO: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




}
