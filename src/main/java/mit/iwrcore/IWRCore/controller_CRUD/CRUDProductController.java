package mit.iwrcore.IWRCore.controller_CRUD;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.FileProduct;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.MaterQuantityDTO;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.SaveProductDTO;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProSDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileProductDTO;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.security.dto.StructureDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@Log4j2
public class CRUDProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProCodeService proCodeService;
    @Autowired
    private FileService fileService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private StructureService structureService;
    @Autowired
    private MaterialService materialService;

    @PostMapping("/saveProduct")
    public void saveProduct(@ModelAttribute SaveProductDTO saveProductDTO,
                            @RequestParam(name = "files", required = false) MultipartFile[] files,
                            @RequestParam(name = "deleteFile", required = false) List<String> deleteFile){
        String jsonList = saveProductDTO.getMaterQuantityDTOs();
        ObjectMapper objectMapper = new ObjectMapper();
        List<MaterQuantityDTO> quantityList=null;

        try {
            quantityList = objectMapper.readValue(jsonList, new TypeReference<List<MaterQuantityDTO>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        ProductDTO productDTO=ProductDTO.builder()
                .manuCode((saveProductDTO.getManuCode()!=null)? saveProductDTO.getManuCode():null)
                .name(saveProductDTO.getProductName())
                .color(saveProductDTO.getProColor())
                .text(saveProductDTO.getProText())
                .supervisor(saveProductDTO.getPerson())
                .mater_imsi(0L)
                .mater_check(0L)
                .build();
        if(saveProductDTO.getSel()==1) productDTO.setMater_imsi(1L);
        if(saveProductDTO.getSel()==2 || saveProductDTO.getSel()==3) {
            productDTO.setMater_imsi(1L);
            productDTO.setMater_check(1L);
        }

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO=(AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO=memberService.findMemberDto(authMemberDTO.getMno(), null);
        productDTO.setMemberDTO(memberDTO);

        ProSDTO proSDTO=proCodeService.findProS(saveProductDTO.getSelectProS());
        productDTO.setProSDTO(proSDTO);

        List<FileProductDTO> exDtoFileList=fileService.getProductFileList(productDTO.getManuCode());
        List<FileProduct> exEntityFileList=new ArrayList<>();
        if(exDtoFileList!=null) exDtoFileList.forEach(x->exEntityFileList.add(fileService.pFile_dTe(x)));

        ProductDTO savedProductDTO=productService.saveProduct(productDTO, exEntityFileList);

        if(deleteFile!=null) fileService.deleteFileRun(deleteFile, "p");
        if(files!=null) fileService.saveFileRun(files, savedProductDTO.getManuCode(), "p");

        List<StructureDTO> stlist=null;
        if(saveProductDTO.getManuCode()!=null){
            stlist=new ArrayList<>(structureService.getStructureByProduct(saveProductDTO.getManuCode()));
            if(stlist.isEmpty()) stlist=null;
        }

        if(stlist==null && quantityList!=null){
            // 새로운 structure 저장
            quantityList.forEach(x->addStructure(savedProductDTO, x, x.getSno()));
        }else if(stlist!=null && quantityList==null){
            // 기존 structure 삭제
            stlist.forEach(x->structureService.deleteStructure(x.getSno()));
        }else if(stlist!=null && quantityList!=null){
            Iterator<StructureDTO> iterator1 = stlist.iterator();
            Iterator<MaterQuantityDTO> iterator2 = quantityList.iterator();
            // 동일한 sno 갖는 structure 저장
            while (iterator1.hasNext()) {
                StructureDTO structure = iterator1.next();
                while (iterator2.hasNext()){
                    MaterQuantityDTO materQuantityDTO=iterator2.next();
                    if (structure.getSno().equals(materQuantityDTO.getSno())) {
                        addStructure(savedProductDTO, materQuantityDTO, materQuantityDTO.getSno());
                        iterator1.remove();
                        iterator2.remove();
                        break;
                    }
                }
            }
            if(stlist.isEmpty()) stlist=null;
            if(quantityList.isEmpty()) quantityList=null;

            // 동일 sno 갖는 structure 저장 후
            if(stlist==null && quantityList!=null) {
                // 남은 새로운 structure 저장
                quantityList.forEach(x->addStructure(savedProductDTO, x, x.getSno()));
            }else if(stlist!=null && quantityList==null) {
                // 남은 기존 structure 삭제
                stlist.forEach(x->structureService.deleteStructure(x.getSno()));
            }else if(stlist!=null && quantityList!=null) {
                // 기존 structure가 많거나 같을 경우
                iterator1 = stlist.iterator();
                iterator2 = quantityList.iterator();
                if(stlist.size()>=quantityList.size()){
                    while (iterator2.hasNext()) {
                        MaterQuantityDTO materQuantityDTO=iterator2.next();
                        while (iterator1.hasNext()){
                            StructureDTO structure = iterator1.next();
                            addStructure(savedProductDTO, materQuantityDTO, structure.getSno());
                            iterator1.remove();
                            iterator2.remove();
                            break;
                        }
                    }
                    if(stlist!=null) stlist.forEach(x->structureService.deleteStructure(x.getSno()));
                    // 기존 structure가 적을 경우
                }else if(stlist.size()<quantityList.size()){
                    while (iterator1.hasNext()) {
                        StructureDTO structure = iterator1.next();
                        while (iterator2.hasNext()){
                            MaterQuantityDTO materQuantityDTO=iterator2.next();
                            addStructure(savedProductDTO, materQuantityDTO, structure.getSno());
                            iterator1.remove();
                            iterator2.remove();
                            break;
                        }
                    }
                    quantityList.forEach(x->addStructure(savedProductDTO, x, x.getSno()));
                }
            }
        }
    }
    private void addStructure(ProductDTO productDTO, MaterQuantityDTO materQuantityDTO, Long sno){
        MaterialDTO materialDTO=materialService.getMaterial(materQuantityDTO.getCode());
        StructureDTO structureDTO=StructureDTO.builder()
                .sno(sno)
                .productDTO(productDTO)
                .materialDTO(materialDTO)
                .quantity(materQuantityDTO.getQuantity())
                .build();
        structureService.saveStructure(structureDTO);
    }
    @GetMapping("/deleteProduct")
    public void deleteProduct(@RequestParam(required = false) Long manuCode){
        List<StructureDTO> structureDTOList=structureService.getStructureByProduct(manuCode);
        structureDTOList.forEach(x->structureService.deleteStructure(x.getSno()));
        List<FileProductDTO> fileList=fileService.getProductFileList(manuCode);
        List<String> deleteFile=new ArrayList<>();
        fileList.forEach(x->deleteFile.add(x.getUuid()));
        fileService.deleteFileRun(deleteFile, "p");
        productService.deleteProduct(manuCode);
    }
}
