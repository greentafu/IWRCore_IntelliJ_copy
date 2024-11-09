package mit.iwrcore.IWRCore.controller_CRUD;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.SaveJodalChasuDTO;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.JodalChasuDTO;
import mit.iwrcore.IWRCore.security.dto.JodalPlanDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.service.JodalChasuService;
import mit.iwrcore.IWRCore.security.service.JodalPlanService;
import mit.iwrcore.IWRCore.security.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class CRUDJodalController {
    @Autowired
    private JodalPlanService jodalPlanService;
    @Autowired
    private JodalChasuService jodalChasuService;
    @Autowired
    private MemberService memberService;

    @PostMapping("/saveJodalChasu")
    public void saveJodalChasu(@RequestBody List<SaveJodalChasuDTO> list){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO = memberService.findMemberDto(authMemberDTO.getMno(), null);

        for(SaveJodalChasuDTO dto:list){
            JodalPlanDTO jodalPlanDTO=jodalPlanService.getJodalPlan(Long.valueOf(dto.getId()));

            List<JodalChasuDTO> jodalChasuDTOs=jodalChasuService.getJodalChasuByJodalPlan(jodalPlanDTO.getJoNo());

            List<Long> longs=new ArrayList<>();
            longs.add(Long.valueOf(dto.getOneNum()));
            longs.add(Long.valueOf(dto.getTwoNum()));
            longs.add(Long.valueOf(dto.getThreeNum()));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<LocalDateTime> localDateTimes=new ArrayList<>();
            localDateTimes.add(LocalDateTime.parse(dto.getOneDate()+" 00:00:00", formatter));
            localDateTimes.add(LocalDateTime.parse(dto.getTwoDate()+" 00:00:00", formatter));
            localDateTimes.add(LocalDateTime.parse(dto.getThreeDate()+" 00:00:00", formatter));

            for(int i=0; i<3; i++){
                JodalChasuDTO jodalChasuDTO=JodalChasuDTO.builder()
                        .jcnum((jodalChasuDTOs.size()!=0)?jodalChasuDTOs.get(i).getJcnum():null)
                        .jodalPlanDTO(jodalPlanDTO)
                        .joNum(longs.get(i))
                        .joDate(localDateTimes.get(i))
                        .memberDTO(memberDTO)
                        .build();
                jodalChasuService.saveJodalChasu(jodalChasuDTO);
            }
        }
    }
    @GetMapping("/deleteJodalChasu")
    public void deleteJodalChasu(@RequestParam(required = false) Long joNo){
        jodalChasuService.deleteJodalChasuByPlan(joNo);
    }
}
