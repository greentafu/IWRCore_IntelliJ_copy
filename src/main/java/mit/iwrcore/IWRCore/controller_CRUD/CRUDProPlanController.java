package mit.iwrcore.IWRCore.controller_CRUD;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.FileProPlan;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileProPlanDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class CRUDProPlanController {
    @Autowired
    private ProplanService proplanService;
    @Autowired
    private LineService lineService;
    @Autowired
    private PlanService planService;
    @Autowired
    private FileService fileService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ProductService productService;
    @Autowired
    private JodalPlanService jodalPlanService;

    @GetMapping("/getLines")
    public List<PlanDTO> getLines(@RequestParam(required = false) Long manuCode){
        List<PlanDTO> planDTOs=planService.getLineByProduct(manuCode);
        return planDTOs;
    }
    @GetMapping("/saveLine")
    public List<Long> saveLine(@RequestParam(required = false) Long manuCode,
                               @RequestParam(required = false) List<Long> quantityList){
        ProductDTO productDTO=productService.getProduct(manuCode);
        List<PlanDTO> planDTOs=planService.getLineByProduct(manuCode);

        List<String> lines=lineService.getLines();
        for(int i=0; i<quantityList.size(); i++){
            Long tempCode=null;
            if(planDTOs!=null){
                PlanDTO planDTO=planService.getLineByLine(manuCode, lines.get(i));
                if(planDTO!=null) tempCode=planDTO.getPlancode();
            }
            PlanDTO planDTO=PlanDTO.builder()
                    .plancode(tempCode)
                    .productDTO(productDTO)
                    .quantity(quantityList.get(i))
                    .line(lines.get(i))
                    .build();
            planService.saveLine(planDTO);
        }
        return quantityList;
    }
    @PostMapping("/savePlan")
    public void savePlan(@ModelAttribute ProplanDTO proplanDTO,
                         @RequestParam(name = "manuCode", required = false) Long manuCode,
                         @RequestParam(name = "startD", required = false) String startD,
                         @RequestParam(name = "endD", required = false) String endD,
                         @RequestParam(name = "lineList", required = false) List<String> lineList,
                         @RequestParam(name = "files", required = false) MultipartFile[] files,
                         @RequestParam(name = "deleteFile", required = false) List<String> deleteFile){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO = memberService.findMemberDto(authMemberDTO.getMno(), null);
        proplanDTO.setMemberDTO(memberDTO);

        ProductDTO productDTO=productService.getProduct(manuCode);
        proplanDTO.setProductDTO(productDTO);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start=LocalDateTime.parse(startD+" 00:00:00", formatter);
        LocalDateTime end=LocalDateTime.parse(endD+" 00:00:00", formatter);
        proplanDTO.setStartDate(start);
        proplanDTO.setEndDate(end);

        proplanDTO.setLine(lineList);

        List<FileProPlanDTO> exDtoFileList=fileService.getProPlanFileList(proplanDTO.getProplanNo());
        List<FileProPlan> exEntityFileList=new ArrayList<>();
        if(exDtoFileList!=null) exDtoFileList.forEach(x->exEntityFileList.add(fileService.ppFile_dTe(x)));

        ProplanDTO savedProplanDTO=proplanService.saveProPlan(proplanDTO, exEntityFileList);
        Long proplanNo=savedProplanDTO.getProplanNo();

        if(deleteFile!=null) fileService.deleteFileRun(deleteFile, "pp");
        if(files!=null) fileService.saveFileRun(files, proplanNo, "pp");

        if(proplanDTO.getProplanNo()==null) jodalPlanService.saveJodalPlanFromProplan(savedProplanDTO, memberDTO);
    }
    @GetMapping("/deleteProPlan")
    public void deleteProPlan(@RequestParam(required = false) Long proplanNo){
        List<FileProPlanDTO> fileList=fileService.getProPlanFileList(proplanNo);
        List<String> deleteFile=new ArrayList<>();
        if(fileList!=null){
            fileList.forEach(x->deleteFile.add(x.getUuid()));
            fileService.deleteFileRun(deleteFile, "pp");
        }

        List<JodalPlanDTO> jodalPlanDTOs=jodalPlanService.getJodalPlanByProPlan(proplanNo);
        if(jodalPlanDTOs!=null) jodalPlanDTOs.forEach(x->jodalPlanService.deleteJodalPlan(x.getJoNo()));

        proplanService.deleteById(proplanNo);
    }
}
