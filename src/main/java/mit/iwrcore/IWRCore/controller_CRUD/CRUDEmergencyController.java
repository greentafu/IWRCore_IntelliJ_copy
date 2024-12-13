package mit.iwrcore.IWRCore.controller_CRUD;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.BaljuDTO;
import mit.iwrcore.IWRCore.security.dto.EmergencyDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.BaljuLLDTO;
import mit.iwrcore.IWRCore.security.service.BaljuService;
import mit.iwrcore.IWRCore.security.service.EmergencyService;
import mit.iwrcore.IWRCore.security.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Log4j2
public class CRUDEmergencyController {
    @Autowired
    private EmergencyService emergencyService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BaljuService baljuService;

    @PostMapping("/saveUrgency")
    public Long saveUrgency(
            @RequestParam(required = false) Long urgencyNum, @RequestParam(required = false) String urgencyDate,
            @RequestParam(required = false) Long proplanNo, @RequestParam(required = false) Long materialCode){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO = memberService.findMemberDto(authMemberDTO.getMno(), null);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date=LocalDateTime.parse(urgencyDate+" 00:00:00", formatter);

        List<BaljuLLDTO> baljuDTOList=baljuService.notFinBaljuByProMater(proplanNo, materialCode);
        int listSize=baljuDTOList.size();

        Long tempQuantity=urgencyNum;
        for(int i=0; i<listSize; i++){
            if(i!=listSize-1){
                BaljuLLDTO dto=baljuDTOList.get(i);
                Long remainNum=dto.getBaljuDTO().getContractDTO().getConNum()-dto.getLong1()+dto.getLong2();
                if(remainNum>=tempQuantity){
                    EmergencyDTO emergencyDTO= EmergencyDTO.builder()
                            .emerNum(urgencyNum)
                            .emerDate(date)
                            .who(memberDTO.getName())
                            .emcheck(0L)
                            .baljuDTO(baljuDTOList.get(0).getBaljuDTO())
                            .memberDTO(memberDTO)
                            .build();
                    emergencyService.saveEmergency(emergencyDTO);
                    break;
                }else{
                    EmergencyDTO emergencyDTO= EmergencyDTO.builder()
                            .emerNum(remainNum)
                            .emerDate(date)
                            .who(memberDTO.getName())
                            .emcheck(0L)
                            .baljuDTO(baljuDTOList.get(0).getBaljuDTO())
                            .memberDTO(memberDTO)
                            .build();
                    emergencyService.saveEmergency(emergencyDTO);
                    tempQuantity=-remainNum;
                }
            }else{
                EmergencyDTO emergencyDTO= EmergencyDTO.builder()
                        .emerNum(tempQuantity)
                        .emerDate(date)
                        .who(memberDTO.getName())
                        .emcheck(0L)
                        .baljuDTO(baljuDTOList.get(0).getBaljuDTO())
                        .memberDTO(memberDTO)
                        .build();
                emergencyService.saveEmergency(emergencyDTO);
            }
        }
        return (long) listSize;
    }
}
