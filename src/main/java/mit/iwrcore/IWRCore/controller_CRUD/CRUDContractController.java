package mit.iwrcore.IWRCore.controller_CRUD;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.FileContract;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.AttachFileDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileContractDTO;
import mit.iwrcore.IWRCore.security.dto.JodalPlanDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.PartnerDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.LLLSDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class CRUDContractController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private FileService fileService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private JodalPlanService jodalPlanService;

    @PostMapping("/saveContract")
    public void saveContract(@RequestParam(name = "conNo", required = false) Long conNo,
                             @RequestParam(name = "person", required = false) String person,
                             @RequestParam(name = "partnerNo", required = false) Long partnerNo,
                             @RequestParam(name = "planData", required = false) String planData,
                             @RequestParam(name = "files", required = false) MultipartFile[] files,
                             @RequestParam(name = "deleteFile", required = false) List<String> deleteFile){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO = memberService.findMemberDto(authMemberDTO.getMno(), null);

        PartnerDTO partnerDTO=partnerService.findPartnerDto(partnerNo, null, null);

        ObjectMapper objectMapper = new ObjectMapper();
        List<LLLSDTO> quantityList=null;
        try {
            quantityList = objectMapper.readValue(planData, new TypeReference<List<LLLSDTO>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<FileContractDTO> exDtoFileList=fileService.getContractFileList(conNo);
        List<FileContract> exEntityFileList=new ArrayList<>();
        if(exDtoFileList!=null) exDtoFileList.forEach(x->exEntityFileList.add(fileService.cFile_dTe(x)));

        List<AttachFileDTO> attachFileDTOs=null;
        if(files!=null) attachFileDTOs=fileService.fileCopy(files, "c", quantityList.size());

        if(quantityList!=null){
            int num=0;
            for(LLLSDTO dto:quantityList){
                JodalPlanDTO jodalPlanDTO=jodalPlanService.getJodalPlan(dto.getLong1());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime date=LocalDateTime.parse(dto.getString()+" 00:00:00", formatter);

                ContractDTO contractDTO=ContractDTO.builder()
                        .conNo(conNo)
                        .who(person)
                        .conNum(dto.getLong3())
                        .money(dto.getLong2())
                        .conDate(date)
                        .jodalPlanDTO(jodalPlanDTO)
                        .memberDTO(memberDTO)
                        .partnerDTO(partnerDTO).build();
                ContractDTO savedContract=contractService.saveContract(contractDTO, exEntityFileList);

                if(deleteFile!=null) fileService.deleteFileRun(deleteFile, "c");

                if(attachFileDTOs!=null) {
                    int fileSize= attachFileDTOs.size();
                    int conSize= quantityList.size();
                    for(int i=num; i<fileSize; i+=conSize){
                        fileService.saveFile(savedContract.getConNo(), attachFileDTOs.get(i), "c");
                    }
                }
                num++;
            }
        }
    }
    @GetMapping("/deleteContract")
    public void deleteContract(@RequestParam(required = false) Long conNo){
        List<FileContractDTO> fileList=fileService.getContractFileList(conNo);
        List<String> deleteFile=new ArrayList<>();
        fileList.forEach(x->deleteFile.add(x.getUuid()));
        fileService.deleteFileRun(deleteFile, "c");
        contractService.deleteContract(conNo);
    }
}
