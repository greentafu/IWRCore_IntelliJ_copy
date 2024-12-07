package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/proteam")
@RequiredArgsConstructor
@Log4j2
public class ProTeamController {

    private final ProductService productService;
    private final ProplanService proplanService;
    private final PreRequestService preRequestService;
    private final RequestService requestService;
    private final LineService lineService;
    private final PlanService planService;
    private final LineStructureService lineStructureService;


    @GetMapping("/list_pro")
    public void list_pro() {}
    @GetMapping("/input_pro")
    public void input_pro(@RequestParam(value = "manuCode", required = false) Long manuCode, Model model) {
        if(manuCode!=null) {
            model.addAttribute("manuCode", manuCode);
            model.addAttribute("productName", productService.getProduct(manuCode).getName());
        }
        model.addAttribute("line_list", lineService.getLines());
    }
    @GetMapping("/modify_plan")
    public void modify_plan(@RequestParam("proplanNo") Long proplanNo, Model model) {
        ProplanDTO proplanDTO=proplanService.getProPlan(proplanNo);

        model.addAttribute("proplan", proplanDTO);
        model.addAttribute("product", proplanDTO.getProductDTO());
        model.addAttribute("line_list", lineService.getLines());

        String tempLine="";
        List<LineStructureDTO> lineStructureDTOs=lineStructureService.getLineStructureByProPlanNo(proplanNo);
        if(lineStructureDTOs!=null){
            for(int i=0; i<lineStructureDTOs.size(); i++){
                tempLine=tempLine+lineStructureDTOs.get(i).getLineDTO().getLineCode();
                if(i+1<lineStructureDTOs.size()) tempLine=tempLine+",";
            }
        }
        model.addAttribute("useLine", tempLine);
    }
    @GetMapping("/details_plan")
    public void details_plan(@RequestParam("proplanNo") Long proplanNo, Model model) {
        ProplanDTO proplanDTO=proplanService.getProPlan(proplanNo);
        Long manuCode=proplanDTO.getProductDTO().getManuCode();

        model.addAttribute("proplan", proplanDTO);
        model.addAttribute("lines", planService.getLineByProduct(manuCode));

        String tempLine="";
        List<LineStructureDTO> lineStructureDTOs=lineStructureService.getLineStructureByProPlanNo(proplanNo);
        if(lineStructureDTOs!=null){
            for(int i=0; i<lineStructureDTOs.size(); i++){
                tempLine=tempLine+lineStructureDTOs.get(i).getLineDTO().getLineName();
                if(i+1<lineStructureDTOs.size()) tempLine=tempLine+",";
            }
        }
        model.addAttribute("useLine", tempLine);
    }

    @GetMapping("/list_request")
    public void list_request() {}
    @GetMapping("/input_request")
    public void input_request() {}
    @GetMapping("/modify_request")
    public void modify_request(@RequestParam("preReqCode") Long preReqCode, Model model) {
        model.addAttribute("preRequestDTO", preRequestService.getPreRequest(preReqCode));
    }
    @GetMapping("/details_request")
    public void details_request(@RequestParam("preReqCode") Long preReqCode, Model model) {
        model.addAttribute("preRequestDTO", preRequestService.getPreRequest(preReqCode));
        model.addAttribute("list", requestService.getStructureStock(preReqCode));
    }
}


