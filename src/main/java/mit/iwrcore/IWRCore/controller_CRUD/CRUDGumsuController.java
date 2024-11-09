package mit.iwrcore.IWRCore.controller_CRUD;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.SaveGumsuDTO;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.BaljuDTO;
import mit.iwrcore.IWRCore.security.dto.GumsuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.GumsuDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.service.BaljuService;
import mit.iwrcore.IWRCore.security.service.GumsuChasuService;
import mit.iwrcore.IWRCore.security.service.GumsuService;
import mit.iwrcore.IWRCore.security.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Log4j2
public class CRUDGumsuController {
    @Autowired
    private GumsuService gumsuService;
    @Autowired
    private GumsuChasuService gumsuChasuService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BaljuService baljuService;

    @PostMapping("/saveGumsu")
    @ResponseBody
    public void saveGumsu(@RequestBody List<SaveGumsuDTO> list){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO=(AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO=memberService.findMemberDto(authMemberDTO.getMno(), null);

        for(SaveGumsuDTO preGumsuDTO:list){
            BaljuDTO baljuDTO=baljuService.getBalju(preGumsuDTO.getBaljuNo());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime oneDay=LocalDateTime.parse(preGumsuDTO.getOneDate()+" 00:00:00", formatter);
            LocalDateTime twoDay=LocalDateTime.parse(preGumsuDTO.getTwoDate()+" 00:00:00", formatter);
            LocalDateTime threeDay=LocalDateTime.parse(preGumsuDTO.getThreeDate()+" 00:00:00", formatter);

            List<LocalDateTime> timeList=new ArrayList<>(Arrays.asList(oneDay, twoDay, threeDay));
            List<Long> numList=new ArrayList<>(Arrays.asList(preGumsuDTO.getOneNum(), preGumsuDTO.getTwoNum(), preGumsuDTO.getThreeNum()));

            if(preGumsuDTO.getGumsuNo()==null) {
                GumsuDTO gumsuDTO = GumsuDTO.builder()
                        .who(preGumsuDTO.getPerson())
                        .make(0L)
                        .memberDTO(memberDTO)
                        .baljuDTO(baljuDTO)
                        .build();
                GumsuDTO savedGumsuDTO=gumsuService.saveGumsu(gumsuDTO);
                for(int i=0; i<3; i++){
                    GumsuChasuDTO gumsuChasuDTO=GumsuChasuDTO.builder()
                            .gumsuNum(numList.get(i))
                            .gumsuDate(timeList.get(i))
                            .gumsuDTO(savedGumsuDTO)
                            .memberDTO(memberDTO)
                            .build();
                    gumsuChasuService.saveGumsuChasu(gumsuChasuDTO);
                }
            }else{
                List<GumsuChasuDTO> gumsuChasuDTOs=gumsuChasuService.getGumsuChasuByGumsu(preGumsuDTO.getGumsuNo());
                for(int i=0; i<gumsuChasuDTOs.size(); i++){
                    GumsuChasuDTO gumsuChasuDTO=gumsuChasuDTOs.get(i);
                    gumsuChasuDTO.setGumsuNum(numList.get(i));
                    gumsuChasuDTO.setGumsuDate(timeList.get(i));
                    gumsuChasuService.modifyGumsuChasu(gumsuChasuDTO);
                }
            }
        }
    }
}
