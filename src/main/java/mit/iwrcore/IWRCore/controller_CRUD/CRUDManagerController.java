package mit.iwrcore.IWRCore.controller_CRUD;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.FileMaterial;
import mit.iwrcore.IWRCore.entity.FileProduct;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterSDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartSDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProSDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileMaterialDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileProductDTO;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.PartnerDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class CRUDManagerController {
    @Autowired
    private PartCodeService partCodeService;
    @Autowired
    private ProCodeService proCodeService;
    @Autowired
    private MaterService materService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private FileService fileService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 카테고리
    @PostMapping("/saveCategory")
    public void saveCategory(
            @RequestParam(required = false) Long type, @RequestParam(required = false) Long lcode,
            @RequestParam(required = false) Long mcode, @RequestParam(required = false) String lname,
            @RequestParam(required = false) String mname, @RequestParam(required = false) String sname){
        if(type==0L){
            if(mcode!=null){
                PartMDTO partMDTO=partCodeService.findPartM(mcode);
                if(sname!=null && sname!=""){
                    PartSDTO partSDTO=PartSDTO.builder().Sname(sname).partMDTO(partMDTO).build();
                    partCodeService.insertPartS(partSDTO);
                }
            }else if(lcode!=null){
                PartLDTO partLDTO=partCodeService.findPartL(lcode);
                if(mname!=null && mname!=""){
                    PartMDTO partMDTO=PartMDTO.builder().Mname(mname).partLDTO(partLDTO).build();
                    PartMDTO savedPartMDTO=partCodeService.insertPartM(partMDTO);
                    if(sname!=null & sname!=""){
                        PartSDTO partSDTO=PartSDTO.builder().Sname(sname).partMDTO(savedPartMDTO).build();
                        partCodeService.insertPartS(partSDTO);
                    }
                }
            }else{
                if(lname!=null && lname!=""){
                    PartLDTO partLDTO=PartLDTO.builder().Lname(lname).build();
                    PartLDTO savedPartLDTO=partCodeService.insertPartL(partLDTO);
                    if(mname!=null && mname!=""){
                        PartMDTO partMDTO=PartMDTO.builder().Mname(mname).partLDTO(savedPartLDTO).build();
                        PartMDTO savedPartMDTO=partCodeService.insertPartM(partMDTO);
                        if(sname!=null && sname!=""){
                            PartSDTO partSDTO=PartSDTO.builder().Sname(sname).partMDTO(savedPartMDTO).build();
                            partCodeService.insertPartS(partSDTO);
                        }
                    }
                }
            }
        }else if(type==1L){
            if(mcode!=null){
                ProMDTO proMDTO=proCodeService.findProM(mcode);
                if(sname!=null && sname!=""){
                    ProSDTO proSDTO=ProSDTO.builder().Sname(sname).proMDTO(proMDTO).build();
                    proCodeService.insertProS(proSDTO);
                }
            }else if(lcode!=null){
                ProLDTO proLDTO=proCodeService.findProL(lcode);
                if(mname!=null && mname!=""){
                    ProMDTO proMDTO=ProMDTO.builder().Mname(mname).proLDTO(proLDTO).build();
                    ProMDTO savedProMDTO=proCodeService.insertProM(proMDTO);
                    if(sname!=null && sname!=""){
                        ProSDTO proSDTO=ProSDTO.builder().Sname(sname).proMDTO(savedProMDTO).build();
                        proCodeService.insertProS(proSDTO);
                    }
                }
            }else{
                if(lname!=null && lname!=""){
                    ProLDTO proLDTO=ProLDTO.builder().Lname(lname).build();
                    ProLDTO savedProLDTO=proCodeService.insertProL(proLDTO);
                    if(mname!=null && mname!=""){
                        ProMDTO proMDTO=ProMDTO.builder().Mname(mname).proLDTO(savedProLDTO).build();
                        ProMDTO savedProMDTO=proCodeService.insertProM(proMDTO);
                        if(sname!=null && sname!=""){
                            ProSDTO proSDTO=ProSDTO.builder().Sname(sname).proMDTO(savedProMDTO).build();
                            proCodeService.insertProS(proSDTO);
                        }
                    }
                }
            }
        }else if(type==2L){
            if(mcode!=null){
                MaterMDTO materMDTO=materService.findMaterM(mcode);
                if(sname!=null && sname!=""){
                    MaterSDTO materSDTO=MaterSDTO.builder().Sname(sname).materMDTO(materMDTO).build();
                    materService.insertMS(materSDTO);
                }
            }else if(lcode!=null){
                MaterLDTO materLDTO=materService.findMaterL(lcode);
                if(mname!=null && mname!=""){
                    MaterMDTO materMDTO=MaterMDTO.builder().Mname(mname).materLDTO(materLDTO).build();
                    MaterMDTO savedMaterMDTO=materService.insertMM(materMDTO);
                    if(sname!=null && sname!=""){
                        MaterSDTO materSDTO=MaterSDTO.builder().Sname(sname).materMDTO(savedMaterMDTO).build();
                        materService.insertMS(materSDTO);
                    }
                }
            }else{
                if(lname!=null && lname!=""){
                    MaterLDTO materLDTO=MaterLDTO.builder().lname(lname).build();
                    MaterLDTO savedMaterLDTO=materService.insertML(materLDTO);
                    if(mname!=null && mname!=""){
                        MaterMDTO materMDTO=MaterMDTO.builder().Mname(mname).materLDTO(savedMaterLDTO).build();
                        MaterMDTO savedMaterMDTO=materService.insertMM(materMDTO);
                        if(sname!=null && sname!=""){
                            MaterSDTO materSDTO=MaterSDTO.builder().Sname(sname).materMDTO(savedMaterMDTO).build();
                            materService.insertMS(materSDTO);
                        }
                    }
                }
            }
        }
    }
    @PostMapping("/modifyCategory")
    public void modifyCategory(
            @RequestParam(required = false) Long category, @RequestParam(required = false) Long type,
            @RequestParam(required = false) Long code, @RequestParam(required = false) String lname,
            @RequestParam(required = false) String mname, @RequestParam(required = false) String sname,
            @RequestParam(required = false) Long lcode, @RequestParam(required = false) Long mcode){
        if(category==0L){
            if(type==0L){
                PartLDTO partLDTO=partCodeService.findPartL(code);
                partLDTO.setLname(lname);
                partCodeService.insertPartL(partLDTO);
            }else if(type==1L){
                PartMDTO partMDTO=partCodeService.findPartM(code);
                partMDTO.setMname(mname);
                if(lcode==null){
                    PartLDTO partLDTO=PartLDTO.builder().Lname(lname).build();
                    PartLDTO savedPartL=partCodeService.insertPartL(partLDTO);
                    partMDTO.setPartLDTO(savedPartL);
                }else{
                    if(partMDTO.getPartLDTO().getPartLcode()!=lcode){
                        PartLDTO partLDTO=partCodeService.findPartL(lcode);
                        partMDTO.setPartLDTO(partLDTO);
                    }
                }
                partCodeService.insertPartM(partMDTO);
            }else{
                PartSDTO partSDTO=partCodeService.findPartS(code);
                partSDTO.setSname(sname);
                if(lcode==null){
                    PartLDTO partLDTO=PartLDTO.builder().Lname(lname).build();
                    PartLDTO savedPartL=partCodeService.insertPartL(partLDTO);
                    PartMDTO partMDTO= PartMDTO.builder().Mname(mname).partLDTO(savedPartL).build();
                    PartMDTO savedPartM=partCodeService.insertPartM(partMDTO);
                    partSDTO.setPartMDTO(savedPartM);
                }else{
                    if(mcode==null){
                        PartLDTO partLDTO=partCodeService.findPartL(lcode);
                        PartMDTO partMDTO=PartMDTO.builder().Mname(mname).partLDTO(partLDTO).build();
                        PartMDTO savedPartM=partCodeService.insertPartM(partMDTO);
                        partSDTO.setPartMDTO(savedPartM);
                    }else{
                        if(partSDTO.getPartMDTO().getPartMcode()!=mcode){
                            PartMDTO partMDTO=partCodeService.findPartM(mcode);
                            partSDTO.setPartMDTO(partMDTO);
                        }
                    }
                }
                partCodeService.insertPartS(partSDTO);
            }
        }else if(category==1L){
            if(type==0L){
                ProLDTO proLDTO=proCodeService.findProL(code);
                proLDTO.setLname(lname);
                proCodeService.insertProL(proLDTO);
            }else if(type==1L){
                ProMDTO proMDTO=proCodeService.findProM(code);
                proMDTO.setMname(mname);
                if(lcode==null){
                    ProLDTO proLDTO=ProLDTO.builder().Lname(lname).build();
                    ProLDTO savedProL=proCodeService.insertProL(proLDTO);
                    proMDTO.setProLDTO(savedProL);
                }else{
                    if(proMDTO.getProLDTO().getProLcode()!=lcode){
                        ProLDTO proLDTO=proCodeService.findProL(lcode);
                        proMDTO.setProLDTO(proLDTO);
                    }
                }
                proCodeService.insertProM(proMDTO);
            }else{
                ProSDTO proSDTO=proCodeService.findProS(code);
                proSDTO.setSname(sname);
                if(lcode==null){
                    ProLDTO proLDTO=ProLDTO.builder().Lname(lname).build();
                    ProLDTO savedProL=proCodeService.insertProL(proLDTO);
                    ProMDTO proMDTO=ProMDTO.builder().Mname(mname).proLDTO(savedProL).build();
                    ProMDTO savedProM=proCodeService.insertProM(proMDTO);
                    proSDTO.setProMDTO(savedProM);
                }else{
                    if(mcode==null){
                        ProLDTO proLDTO=proCodeService.findProL(lcode);
                        ProMDTO proMDTO=ProMDTO.builder().Mname(mname).proLDTO(proLDTO).build();
                        ProMDTO savedProM=proCodeService.insertProM(proMDTO);
                        proSDTO.setProMDTO(savedProM);
                    }else{
                        if(proSDTO.getProMDTO().getProLDTO().getProLcode()!=mcode){
                            ProMDTO proMDTO=proCodeService.findProM(mcode);
                            proSDTO.setProMDTO(proMDTO);
                        }
                    }
                }
                proCodeService.insertProS(proSDTO);
            }
        }else if(category==2L){
            if(type==0L){
                MaterLDTO materLDTO=materService.findMaterL(code);
                materLDTO.setLname(lname);
                materService.insertML(materLDTO);
            }else if(type==1L){
                MaterMDTO materMDTO=materService.findMaterM(code);
                materMDTO.setMname(mname);
                if(lcode==null){
                    MaterLDTO materLDTO=MaterLDTO.builder().lname(lname).build();
                    MaterLDTO savedMaterL=materService.insertML(materLDTO);
                    materMDTO.setMaterLDTO(savedMaterL);
                }else{
                    if(materMDTO.getMaterLDTO().getMaterLcode()!=lcode){
                        MaterLDTO materLDTO=materService.findMaterL(lcode);
                        materMDTO.setMaterLDTO(materLDTO);
                    }
                }
                materService.insertMM(materMDTO);
            }else{
                MaterSDTO materSDTO=materService.findMaterS(code);
                materSDTO.setSname(sname);
                if(lcode==null){
                    MaterLDTO materLDTO=MaterLDTO.builder().lname(lname).build();
                    MaterLDTO savedMaterL=materService.insertML(materLDTO);
                    MaterMDTO materMDTO=MaterMDTO.builder().Mname(mname).materLDTO(savedMaterL).build();
                    MaterMDTO savedMaterM=materService.insertMM(materMDTO);
                    materSDTO.setMaterMDTO(savedMaterM);
                }else{
                    if(mcode==null){
                        MaterLDTO materLDTO=materService.findMaterL(lcode);
                        MaterMDTO materMDTO=MaterMDTO.builder().Mname(mname).materLDTO(materLDTO).build();
                        MaterMDTO savedMaterM=materService.insertMM(materMDTO);
                        materSDTO.setMaterMDTO(savedMaterM);
                    }else{
                        if(materSDTO.getMaterMDTO().getMaterLDTO().getMaterLcode()!=mcode){
                            MaterMDTO materMDTO=materService.findMaterM(mcode);
                            materSDTO.setMaterMDTO(materMDTO);
                        }
                    }
                }
                materService.insertMS(materSDTO);
            }
        }
    }
    @PostMapping("/deleteCategory")
    public void deleteCategory(
            @RequestParam(required = false) Long category, @RequestParam(required = false) Long type,
            @RequestParam(required = false) Long code){
        if(category==0L){
            List<PartnerDTO> partnerDTOs=partnerService.getPartnerByCategory(type, code);
            PartSDTO partSDTO=partCodeService.findPartS(1L);
            for(PartnerDTO partnerDTO:partnerDTOs){
                partnerDTO.setPartSDTO(partSDTO);
                partnerService.insertPartner(partnerDTO);
            }
            if(type==0L){
                List<PartMDTO> partMDTOs=partCodeService.findListPartM(partCodeService.findPartL(code), null, null, 0L);
                for(PartMDTO deleteM:partMDTOs){
                    List<PartSDTO> partSDTOs=partCodeService.findListPartS(null, deleteM, null, 0L);
                    for(PartSDTO deleteS:partSDTOs) partCodeService.deletePartS(deleteS.getPartScode());
                    partCodeService.deletePartM(deleteM.getPartMcode());
                }
                partCodeService.deletePartL(code);
            }else if(type==1L){
                List<PartSDTO> partSDTOs=partCodeService.findListPartS(null, partCodeService.findPartM(code), null, 0L);
                for(PartSDTO deleteS:partSDTOs) partCodeService.deletePartS(deleteS.getPartScode());
                partCodeService.deletePartM(code);
            }else{
                partCodeService.deletePartS(code);
            }
        }else if(category==1L){
            List<ProductDTO> productDTOs=productService.getProductByCategory(type, code);
            ProSDTO proSDTO=proCodeService.findProS(1L);
            for(ProductDTO productDTO:productDTOs){
                List<FileProductDTO> exDtoFileList=fileService.getProductFileList(productDTO.getManuCode());
                List<FileProduct> exEntityFileList=new ArrayList<>();
                if(exDtoFileList!=null) exDtoFileList.forEach(x->exEntityFileList.add(fileService.pFile_dTe(x)));
                productDTO.setProSDTO(proSDTO);
                productService.saveProduct(productDTO, exEntityFileList);
            }
            if(type==0L){
                List<ProMDTO> proMDTOs=proCodeService.findListProM(proCodeService.findProL(code), null, null, 0L);
                for(ProMDTO deleteM:proMDTOs){
                    List<ProSDTO> proSDTOs=proCodeService.findListProS(null, deleteM, null, 0L);
                    for(ProSDTO deleteS:proSDTOs) proCodeService.deleteProS(deleteS.getProScode());
                    proCodeService.deleteProM(deleteM.getProMcode());
                }
                proCodeService.deleteProL(code);
            }else if(type==1L){
                List<ProSDTO> proSDTOs=proCodeService.findListProS(null, proCodeService.findProM(code), null, 0L);
                for(ProSDTO deleteS:proSDTOs) proCodeService.deleteProS(deleteS.getProScode());
                proCodeService.deleteProM(code);
            }else{
                proCodeService.deleteProS(code);
            }
        }else if(category==2L){
            List<MaterialDTO> materialDTOs=materialService.getMaterialByCategory(type, code);
            MaterSDTO materSDTO=materService.findMaterS(1L);
            for(MaterialDTO materialDTO:materialDTOs){
                List<FileMaterialDTO> exDtoFileList=fileService.getMaterialFileList(materialDTO.getMaterCode());
                List<FileMaterial> exEntityFileList=new ArrayList<>();
                if(exDtoFileList!=null) exDtoFileList.forEach(x->exEntityFileList.add(fileService.mFile_dTe(x)));
                materialDTO.setMaterSDTO(materSDTO);
                materialService.saveMaterial(materialDTO, exEntityFileList);
            }
            if(type==0L){
                List<MaterMDTO> materMDTOs=materService.findListMaterM(materService.findMaterL(code), null, null, 0L);
                for(MaterMDTO deleteM:materMDTOs){
                    List<MaterSDTO> materSDTOs=materService.findListMaterS(null, deleteM, null, 0L);
                    for(MaterSDTO deleteS:materSDTOs) materService.deleteMaterS(deleteS.getMaterScode());
                    materService.deleteMaterM(deleteM.getMaterMcode());
                }
                materService.deleteMaterL(code);
            }else if(type==1L){
                List<MaterSDTO> materSDTOs=materService.findListMaterS(null, materService.findMaterM(code), null, 0L);
                for(MaterSDTO deleteS:materSDTOs) materService.deleteMaterS(deleteS.getMaterScode());
                materService.deleteMaterM(code);
            }else{
                materService.deleteMaterS(code);
            }
        }
    }

    // 직원, 회사
    @PostMapping("/saveMember")
    public void saveMember(@RequestParam(required = false) Long mno, @RequestParam String name, @RequestParam String phonenumber,
                             @RequestParam String department, @RequestParam Long role,
                             @RequestParam(required = false) String id, @RequestParam(required = false) String pw){
        String temp_pw=(pw!=null && pw!="")?pw:"1111";
        String temp_id=null;
        if(id!=null && id!="") temp_id=id;
        else if(mno!=null && (id==null || id=="")){
            String head="";
            switch(department) {
                case "자재부서": head="mate"; break;
                case "개발부서": head="deve"; break;
                case "생산부서": head="prod";
            }
            temp_id=head+mno+"_"+phonenumber.substring(9);
        }

        MemberDTO memberDTO=MemberDTO.builder()
                .mno((mno!=null)?mno:null).name(name).phonenumber(phonenumber).department(department)
                .id(temp_id).pw(temp_pw).password(passwordEncoder.encode(temp_pw))
                .autoJodalChasu(0L).autoBaljuChasu(0L).autoGumsuChasu(0L)
                .build();

        if(mno!=null) {
            MemberDTO exMemberDTO=memberService.findMemberDto(mno, null);
            memberDTO.setAutoJodalChasu(exMemberDTO.getAutoJodalChasu());
            memberDTO.setAutoBaljuChasu(exMemberDTO.getAutoBaljuChasu());
            memberDTO.setAutoGumsuChasu(exMemberDTO.getAutoGumsuChasu());
        }

        memberService.insertMember(memberDTO, role);
    }
    @PostMapping("/deleteMember")
    public void deleteMember(@RequestParam(required = false) Long mno){
        memberService.deleteMember(mno);
    }
    @PostMapping ("/savePartner")
    public void savePartner(
            @RequestParam(required = false) Long pno, @RequestParam(required = false) String name, @RequestParam(required = false) String registerNum,
            @RequestParam(required = false) String ceo, @RequestParam(required = false) String location, @RequestParam(required = false) String type,
            @RequestParam(required = false) String sector, @RequestParam(required = false) String phonenumber, @RequestParam(required = false) String faxnumber,
            @RequestParam(required = false) String email, @RequestParam(required = false) Long selectPartS, @RequestParam(required = false) String cname,
            @RequestParam(required = false) String cnumber, @RequestParam(required = false) String cmail, @RequestParam(required = false) String id,
            @RequestParam(required = false) String pw){
        Long temp_pno=(pno!=null)?pno:null;
        String temp_pw=(pw!=null && pw!="")?pw:"1111";
        String temp_id=null;
        if(id!=null && id!="") temp_id=id;
        else if(pno!=null && (id==null || id=="")){
            temp_id="partner" + pno + "_" + registerNum.substring(7);
        }

        PartSDTO partSDTO=partCodeService.findPartS(selectPartS);
        PartnerDTO partnerDTO=PartnerDTO.builder()
                .pno(temp_pno).name(name).registrationNumber(registerNum)
                .ceo(ceo).location(location).type(type).sector(sector)
                .telNumber(phonenumber).faxNumber(faxnumber).email(email)
                .partSDTO(partSDTO)
                .contacter(cname).contacterNumber(cnumber).contacterEmail(cmail)
                .id(temp_id).pw(temp_pw).password(passwordEncoder.encode(temp_pw)).build();
        partnerService.insertPartner(partnerDTO);
    }
    @PostMapping("/deletePartner")
    public void deletePartner(@RequestParam(required = false) Long pno){
        partnerService.deletePartner(pno);
    }
}
