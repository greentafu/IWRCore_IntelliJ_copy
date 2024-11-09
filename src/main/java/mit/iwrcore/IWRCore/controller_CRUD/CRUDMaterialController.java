package mit.iwrcore.IWRCore.controller_CRUD;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.FileMaterial;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.BoxDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileMaterialDTO;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class CRUDMaterialController {
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterService materService;
    @Autowired
    private FileService fileService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BoxService boxService;

    @PostMapping("/saveMaterial")
    public void saveMaterial(@ModelAttribute MaterialDTO materialDTO,
                             @RequestParam(name = "box") Long box,
                             @RequestParam(name = "materS") Long materS,
                             @RequestParam(name = "files", required = false) MultipartFile[] files,
                             @RequestParam(name = "deleteFile", required = false) List<String> deleteFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO = memberService.findMemberDto(authMemberDTO.getMno(), null);

        materialDTO.setMemberDTO(memberDTO);
        materialDTO.setBoxDTO(BoxDTO.builder().boxcode(boxService.getBox(box).getBoxcode()).build());
        materialDTO.setMaterSDTO(materService.findMaterS(materS));

        List<FileMaterialDTO> exDtoFileList=fileService.getMaterialFileList(materialDTO.getMaterCode());
        List<FileMaterial> exEntityFileList=new ArrayList<>();
        if(exDtoFileList!=null) exDtoFileList.forEach(x->exEntityFileList.add(fileService.mFile_dTe(x)));

        MaterialDTO savedMaterial=materialService.saveMaterial(materialDTO, exEntityFileList);

        if(deleteFile!=null) fileService.deleteFileRun(deleteFile, "m");
        if(files!=null) fileService.saveFileRun(files, savedMaterial.getMaterCode(), "m");
    }
    @GetMapping("/deleteMaterial")
    public void deleteMaterial(@RequestParam(required = false) Long materCode){
        List<FileMaterialDTO> fileList=fileService.getMaterialFileList(materCode);
        List<String> deleteFile=new ArrayList<>();
        fileList.forEach(x->deleteFile.add(x.getUuid()));
        fileService.deleteFileRun(deleteFile, "m");
        materialService.deleteMaterial(materCode);
    }
}
