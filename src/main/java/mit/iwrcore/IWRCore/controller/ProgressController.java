package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/progress")
@RequiredArgsConstructor
public class ProgressController {
    private final GumsuChasuService gumsuChasuService;
    private final BaljuService baljuService;
    private final BaljuChasuService baljuChasuService;

    @GetMapping("/requiring_progress")
    public void requiring_progress(){}
    @GetMapping("/list_progress")
    public void list_progress(){}
    @GetMapping("/add_progress")
    public void add_progress(@RequestParam(required = false) Long baljuNo, Model model){
        Long pno=0L;
        String name="";
        if(baljuNo!=null){
            PartnerDTO partnerDTO=baljuService.getBalju(baljuNo).getContractDTO().getPartnerDTO();
            pno=partnerDTO.getPno();
            name=partnerDTO.getName();
        }
        model.addAttribute("pno", pno);
        model.addAttribute("name", name);
    }
    @GetMapping("/modify_progress")
    public void modify_progress(Long gumsuNo, Model model){
        List<GumsuChasuDTO> gumsuChasuDTOs=gumsuChasuService.getGumsuChasuByGumsu(gumsuNo);
        GumsuDTO gumsuDTO=gumsuChasuDTOs.get(0).getGumsuDTO();
        BaljuDTO baljuDTO=gumsuDTO.getBaljuDTO();
        PartnerDTO partnerDTO=baljuDTO.getContractDTO().getPartnerDTO();
        String materialName=baljuDTO.getContractDTO().getJodalPlanDTO().getMaterialDTO().getName();
        List<BaljuChasuDTO> baljuChasuDTOs=baljuChasuService.getBaljuChasuListByBaljuNo(baljuDTO.getBaljuNo());

        model.addAttribute("baljuList", baljuChasuDTOs);
        model.addAttribute("gumsu", gumsuDTO);
        model.addAttribute("baljuNo", baljuDTO.getBaljuNo());
        model.addAttribute("partner", partnerDTO);
        model.addAttribute("materialName", materialName);
        model.addAttribute("gumsuList", gumsuChasuDTOs);
    }
}
