package mit.iwrcore.IWRCore.controller_CRUD;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.FileReturns;
import mit.iwrcore.IWRCore.entity.Member;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileReturnsDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.ReturnsDTO;
import mit.iwrcore.IWRCore.security.dto.ShipmentDTO;
import mit.iwrcore.IWRCore.security.service.FileService;
import mit.iwrcore.IWRCore.security.service.MemberService;
import mit.iwrcore.IWRCore.security.service.ReturnsService;
import mit.iwrcore.IWRCore.security.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class CRUDShipmentController {
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private ReturnsService returnsService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private FileService fileService;

    @PostMapping("/saveReceiveShipment")
    public void saveReceiveShipment(@RequestParam(required = false) Long shipNo){
        LocalDateTime now=LocalDateTime.now();
        shipmentService.updateShipmentDate(now, shipNo);
    }
    @PostMapping("/saveGetShipment")
    public void saveGetShipment(@RequestParam(required = false) Long shipNo){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO=(AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO=memberService.findMemberDto(authMemberDTO.getMno(), null);
        Member member=memberService.memberdtoToEntity(memberDTO);
        shipmentService.updateMemberCheck(member, shipNo);
    }
    @PostMapping("/saveReturnShipment")
    public void saveReturnShipment(@RequestParam(name = "shipNo") Long shipNo,
                                   @RequestParam(name = "email") String email,
                                   @RequestParam(name = "returnType") String returnType,
                                   @RequestParam(name = "returnReason") String returnReason,
                                   @RequestParam(name = "returnText") String returnText,
                                   @RequestParam(name = "files", required = false) MultipartFile[] files,
                                   @RequestParam(name = "deleteFile", required = false) List<String> deleteFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO = memberService.findMemberDto(authMemberDTO.getMno(), null);

        // returnsService.addReturns(returnsDTO, memberDTO, shipNo);
        ShipmentDTO shipmentDTO=shipmentService.getShipment(shipNo);
        ReturnsDTO returnsDTO=ReturnsDTO.builder()
                .email(email)
                .whyRe(returnType)
                .reDetail(returnReason)
                .bGo(returnText)
                .returnCheck(0L)
                .shipmentDTO(shipmentDTO)
                .memberDTO(memberDTO).build();

        List<FileReturnsDTO> exDtoFileList=fileService.getReturnsFileList(returnsDTO.getReNO());
        List<FileReturns> exEntityFileList=new ArrayList<>();
        if(exDtoFileList!=null) exDtoFileList.forEach(x->exEntityFileList.add(fileService.rFile_dTe(x)));

        ReturnsDTO savedReturns=returnsService.saveReturns(returnsDTO, exEntityFileList);

        if(deleteFile!=null) fileService.deleteFileRun(deleteFile, "r");
        if(files!=null) fileService.saveFileRun(files, savedReturns.getReNO(), "r");
    }
}
