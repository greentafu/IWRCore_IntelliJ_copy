package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.SaveJodalChasuDTO;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.multiDTO.JodalChasuDateDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanSturcture2DTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanSturctureDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/jodal")
@Log4j2
@RequiredArgsConstructor
public class JodalController {

    private final JodalPlanService jodalPlanService;
    private final ContractService contractService;
    private final StructureService structureService;
    private final JodalChasuService jodalChasuService;
    private final MemberService memberService;

    @GetMapping("/list_jodal")
    public void list_jodal() {}
    @GetMapping("/jodal_ready")
    public void jodalReady(@RequestParam(required = false) Long joNo, Model model) {
        JodalPlanDTO jodalPlanDTO=jodalPlanService.getJodalPlan(joNo);
        model.addAttribute("jodalPlan", jodalPlanDTO);

        List<ProPlanSturctureDTO> list=jodalChasuService.modifyJodalChasu(jodalPlanDTO.getProplanDTO().getProplanNo());

        LocalDateTime startDate=list.get(0).getProplanDTO().getStartDate();
        LocalDateTime minusDate=startDate.minusDays(3L);
        model.addAttribute("baseDate", minusDate);

        // 최종적으로 보낼 list
        List<ProPlanSturcture2DTO> dtoList=new ArrayList<>();

        // 임시저장용
        int realsize=list.size();
        int tempsize=0;
        Long tempJoNo=0L;

        ProplanDTO tempProPlanDTO=null;
        StructureDTO tempStructureDTO=null;
        Long tempSumRequest=0L;
        Long tempSumShip=0L;
        JodalPlanDTO tempJodalPlanDTO=null;
        List<JodalChasuDateDTO> tempDtoList=new ArrayList<>();

        // 반복문
        for(ProPlanSturctureDTO item:list){
            if(tempJoNo==0L) {
                tempJoNo=item.getJodalPlanDTO().getJoNo();
            }else if(tempJoNo!=item.getJodalPlanDTO().getJoNo()){
                dtoList.add(new ProPlanSturcture2DTO(tempProPlanDTO, tempStructureDTO, tempSumRequest,
                        tempSumShip, tempJodalPlanDTO, (tempDtoList.size()!=0)?new ArrayList<>(tempDtoList):null, null));
                tempDtoList.clear();
                tempJoNo=item.getJodalPlanDTO().getJoNo();
            }
            tempProPlanDTO=item.getProplanDTO();
            tempStructureDTO=item.getStructureDTO();
            tempSumRequest= item.getSumRequest();
            tempSumShip= item.getSumShip();
            tempJodalPlanDTO=item.getJodalPlanDTO();

            if(item.getJodalChasuDTO()!=null){
                Long jcnum=item.getJodalChasuDTO().getJcnum();
                Long juNum=item.getJodalChasuDTO().getJoNum();
                String joDate=item.getJodalChasuDTO().getJoDate().toString().split("T")[0];
                tempDtoList.add(new JodalChasuDateDTO(jcnum, juNum, joDate));
            }

            tempsize+=1;

            if(tempsize==realsize){
                dtoList.add(new ProPlanSturcture2DTO(tempProPlanDTO, tempStructureDTO, tempSumRequest,
                        tempSumShip, tempJodalPlanDTO, (tempDtoList.size()!=0)?new ArrayList<>(tempDtoList):null, null));
            }
        }

        model.addAttribute("structure_list", dtoList);
    }
    @GetMapping("/modify_jodal")
    public void modify_jodal(@RequestParam(required = false) Long joNo, Model model){
        JodalPlanDTO jodalPlanDTO=jodalPlanService.getJodalPlan(joNo);
        model.addAttribute("jodalPlan", jodalPlanDTO);

        List<ProPlanSturctureDTO> list=jodalChasuService.modifyJodalChasu(jodalPlanDTO.getProplanDTO().getProplanNo());

        LocalDateTime startDate=list.get(0).getProplanDTO().getStartDate();
        LocalDateTime minusDate=startDate.minusDays(3L);
        model.addAttribute("baseDate", minusDate);

        // 최종적으로 보낼 list
        List<ProPlanSturcture2DTO> dtoList=new ArrayList<>();

        // 임시저장용
        int realsize=list.size();
        int tempsize=0;
        Long tempJoNo=0L;

        ProplanDTO tempProPlanDTO=null;
        StructureDTO tempStructureDTO=null;
        Long tempSumRequest=0L;
        Long tempSumShip=0L;
        JodalPlanDTO tempJodalPlanDTO=null;
        List<JodalChasuDateDTO> tempDtoList=new ArrayList<>();
        Long tempCountContract=0L;

        // 반복문
        for(ProPlanSturctureDTO item:list){
            if(tempJoNo==0L) {
                tempJoNo=item.getJodalPlanDTO().getJoNo();
            }else if(tempJoNo!=item.getJodalPlanDTO().getJoNo()){
                dtoList.add(new ProPlanSturcture2DTO(tempProPlanDTO, tempStructureDTO, tempSumRequest,
                        tempSumShip, tempJodalPlanDTO, (tempDtoList.size()!=0)?new ArrayList<>(tempDtoList):null, tempCountContract));
                tempDtoList.clear();
                tempJoNo=item.getJodalPlanDTO().getJoNo();
            }
            tempProPlanDTO=item.getProplanDTO();
            tempStructureDTO=item.getStructureDTO();
            tempSumRequest= item.getSumRequest();
            tempSumShip= item.getSumShip();
            tempJodalPlanDTO=item.getJodalPlanDTO();
            tempCountContract=item.getCountContract();

            if(item.getJodalChasuDTO()!=null){
                Long jcnum=item.getJodalChasuDTO().getJcnum();
                Long juNum=item.getJodalChasuDTO().getJoNum();
                String joDate=item.getJodalChasuDTO().getJoDate().toString().split("T")[0];
                tempDtoList.add(new JodalChasuDateDTO(jcnum, juNum, joDate));
            }

            tempsize+=1;

            if(tempsize==realsize){
                dtoList.add(new ProPlanSturcture2DTO(tempProPlanDTO, tempStructureDTO, tempSumRequest,
                        tempSumShip, tempJodalPlanDTO, (tempDtoList.size()!=0)?new ArrayList<>(tempDtoList):null, tempCountContract));
            }
        }

        model.addAttribute("structure_list", dtoList);
    }
    @GetMapping("/jodal")
    public void jodal(@RequestParam(required = false) Long joNo, Model model) {
        JodalPlanDTO jodalPlanDTO=jodalPlanService.getJodalPlan(joNo);
        model.addAttribute("jodalPlan", jodalPlanDTO);

        List<ProPlanSturctureDTO> list=jodalChasuService.modifyJodalChasu(jodalPlanDTO.getProplanDTO().getProplanNo());

        LocalDateTime startDate=list.get(0).getProplanDTO().getStartDate();
        LocalDateTime minusDate=startDate.minusDays(3L);
        model.addAttribute("baseDate", minusDate);

        // 최종적으로 보낼 list
        List<ProPlanSturcture2DTO> dtoList=new ArrayList<>();

        // 임시저장용
        int realsize=list.size();
        int tempsize=0;
        Long tempJoNo=0L;

        ProplanDTO tempProPlanDTO=null;
        StructureDTO tempStructureDTO=null;
        Long tempSumRequest=0L;
        Long tempSumShip=0L;
        JodalPlanDTO tempJodalPlanDTO=null;
        List<JodalChasuDateDTO> tempDtoList=new ArrayList<>();

        // 반복문
        for(ProPlanSturctureDTO item:list){
            if(tempJoNo==0L) {
                tempJoNo=item.getJodalPlanDTO().getJoNo();
            }else if(tempJoNo!=item.getJodalPlanDTO().getJoNo()){
                dtoList.add(new ProPlanSturcture2DTO(tempProPlanDTO, tempStructureDTO, tempSumRequest,
                        tempSumShip, tempJodalPlanDTO, (tempDtoList.size()!=0)?new ArrayList<>(tempDtoList):null, null));
                tempDtoList.clear();
                tempJoNo=item.getJodalPlanDTO().getJoNo();
            }
            tempProPlanDTO=item.getProplanDTO();
            tempStructureDTO=item.getStructureDTO();
            tempSumRequest= item.getSumRequest();
            tempSumShip= item.getSumShip();
            tempJodalPlanDTO=item.getJodalPlanDTO();

            if(item.getJodalChasuDTO()!=null){
                Long jcnum=item.getJodalChasuDTO().getJcnum();
                Long juNum=item.getJodalChasuDTO().getJoNum();
                String joDate=item.getJodalChasuDTO().getJoDate().toString().split("T")[0];
                tempDtoList.add(new JodalChasuDateDTO(jcnum, juNum, joDate));
            }

            tempsize+=1;

            if(tempsize==realsize){
                dtoList.add(new ProPlanSturcture2DTO(tempProPlanDTO, tempStructureDTO, tempSumRequest,
                        tempSumShip, tempJodalPlanDTO, (tempDtoList.size()!=0)?new ArrayList<>(tempDtoList):null, null));
            }
        }

        model.addAttribute("structure_list", dtoList);
    }
}
