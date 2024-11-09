package mit.iwrcore.IWRCore.controller_CRUD;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.SaveBaljuDTO;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.BaljuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.BaljuDTO;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.service.BaljuChasuService;
import mit.iwrcore.IWRCore.security.service.BaljuService;
import mit.iwrcore.IWRCore.security.service.ContractService;
import mit.iwrcore.IWRCore.security.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Log4j2
public class CRUDBaljuController {
    @Autowired
    private BaljuService baljuService;
    @Autowired
    private BaljuChasuService baljuChasuService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ContractService contractService;

    @PostMapping("/saveBalju")
    @ResponseBody
    public void saveBalju(@RequestBody List<SaveBaljuDTO> baljuDataList){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO=(AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO=memberService.findMemberDto(authMemberDTO.getMno(), null);

        for(SaveBaljuDTO preBaljuDTO:baljuDataList){
            ContractDTO contractDTO=contractService.getContract(preBaljuDTO.getConNo());
            Long sum=preBaljuDTO.getOneNum()+preBaljuDTO.getTwoNum()+preBaljuDTO.getThreeNum();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime oneDay=LocalDateTime.parse(preBaljuDTO.getOneDate()+" 00:00:00", formatter);
            LocalDateTime twoDay=LocalDateTime.parse(preBaljuDTO.getTwoDate()+" 00:00:00", formatter);
            LocalDateTime threeDay=LocalDateTime.parse(preBaljuDTO.getThreeDate()+" 00:00:00", formatter);

            List<LocalDateTime> timeList=new ArrayList<>(Arrays.asList(oneDay, twoDay, threeDay));
            List<Long> numList=new ArrayList<>(Arrays.asList(preBaljuDTO.getOneNum(), preBaljuDTO.getTwoNum(), preBaljuDTO.getThreeNum()));

            if(preBaljuDTO.getBaljuNo()==null){
                BaljuDTO baljuDTO=BaljuDTO.builder()
                        .baljuNo(preBaljuDTO.getBaljuNo())
                        .baljuNum(sum)
                        .baljuDate(oneDay)
                        .baljuWhere(preBaljuDTO.getBaljuWhere())
                        .baljuPlz(preBaljuDTO.getBaljuPlz())
                        .finCheck(0L)
                        .contractDTO(contractDTO)
                        .memberDTO(memberDTO).build();
                BaljuDTO savedBaljuDTO=baljuService.saveBalju(baljuDTO);
                for(int i=0; i<3; i++){
                    BaljuChasuDTO baljuChasuDTO=BaljuChasuDTO.builder()
                            .balNum(numList.get(i))
                            .balDate(timeList.get(i))
                            .baljuDTO(savedBaljuDTO)
                            .build();
                    baljuChasuService.saveBaljuChasu(baljuChasuDTO);
                }
            }else{
                BaljuDTO baljuDTO=baljuService.getBalju(preBaljuDTO.getBaljuNo());
                baljuDTO.setBaljuNum(numList.stream().mapToLong(Long::longValue).sum());
                baljuDTO.setBaljuDate(oneDay);
                baljuDTO.setBaljuWhere(preBaljuDTO.getBaljuWhere());
                baljuDTO.setBaljuPlz(preBaljuDTO.getBaljuPlz());
                BaljuDTO savedBaljuDTO=baljuService.modifyBalju(baljuDTO);

                List<BaljuChasuDTO> baljuChasuDTOs=baljuChasuService.getBaljuChasuListByBaljuNo(savedBaljuDTO.getBaljuNo());
                for(int i=0; i<baljuChasuDTOs.size(); i++){
                    BaljuChasuDTO baljuChasuDTO=baljuChasuDTOs.get(i);
                    baljuChasuDTO.setBalNum(numList.get(i));
                    baljuChasuDTO.setBalDate(timeList.get(i));
                    baljuChasuService.modifyBalhuChasu(baljuChasuDTO);
                }
            }
        }
    }
    @PostMapping("/deleteBalju")
    public void deleteBalju(@RequestParam(required = false) Long baljuNo){
        baljuService.deleteBalju(baljuNo);
    }
}
