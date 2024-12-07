package mit.iwrcore.IWRCore.controller_CRUD;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.PreRequestDTO;
import mit.iwrcore.IWRCore.security.dto.RequestDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.LLLLLLSSDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Log4j2
public class CRUDRequestController {
    @Autowired
    private RequestService requestService;
    @Autowired
    private LineService lineService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private ProplanService proplanService;
    @Autowired
    private PreRequestService preRequestService;

    @PostMapping("/saveRequest")
    public void saveProduct(@RequestBody(required = false) List<LLLLLLSSDTO> requestDataList){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO = memberService.findMemberDto(authMemberDTO.getMno(), null);

        LLLLLLSSDTO temp=requestDataList.get(0);
        PreRequestDTO preRequestDTO=PreRequestDTO.builder()
                .preReqCode(temp.getLong1())
                .text(temp.getString1())
                .allCheck(0L)
                .lineDTO(lineService.getOneLine(temp.getLong2()))
                .proplanDTO(proplanService.getProPlan(temp.getLong3()))
                .memberDTO(memberDTO)
                .build();
        PreRequestDTO savedPreRequest=preRequestService.savePreRequest(preRequestDTO);

        for(LLLLLLSSDTO dto:requestDataList){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date=LocalDateTime.parse(dto.getString2()+" 00:00:00", formatter);

            RequestDTO requestDTO=RequestDTO.builder()
                    .requestCode(dto.getLong4())
                    .materialDTO(materialService.getMaterial(dto.getLong5()))
                    .requestNum(dto.getLong6())
                    .eventDate(date)
                    .preRequestDTO(savedPreRequest)
                    .build();
            if(dto.getLong6()==0L) requestDTO.setReleaseDate(date);
            requestService.saveRequest(requestDTO);
        }
    }
    @GetMapping("/deleteRequest")
    public void deleteRequest(@RequestParam(required = false) Long preReqCode){
        List<RequestDTO> requestDTOList=requestService.getRequestByPreRequest(preReqCode);
        requestDTOList.forEach(x->requestService.deleteRequest(x.getRequestCode()));
        preRequestService.deletePreRequest(preReqCode);
    }
    @PostMapping("/saveRequestCheck")
    public void saveRequestCheck(@RequestParam(required = false) Long reqCode){
        requestService.updateReqCheck(reqCode);
    }
}
