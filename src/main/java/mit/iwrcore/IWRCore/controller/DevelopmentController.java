package mit.iwrcore.IWRCore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.Structure;
import mit.iwrcore.IWRCore.repository.MaterialRepository;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.MaterQuantityDTO;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.SaveProductDTO;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.MemberDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.ProDTO.ProCodeListDTO;
import mit.iwrcore.IWRCore.security.dto.ProDTO.ProSDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.security.dto.StructureDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/development")
@RequiredArgsConstructor
@Log4j2
public class DevelopmentController {

    private final ProductService productService;
    private final MaterialService materialService;
    private final MaterialRepository materialRepository;
    private final MemberService memberService;
    private final ProCodeService proCodeService;
    private final StructureService structureService;

    @GetMapping("/input_dev")
    public void input_dev(PageRequestDTO pageRequestDTO, Model model){
        Integer size=(int)materialRepository.count();
        pageRequestDTO.setSize((size!=null)?((size>=1)?size:1):1);
        model.addAttribute("material_list", materialService.findMaterialAll(pageRequestDTO));
    }
    @GetMapping("/list_dev")
    public void list_dev(PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("product_list", productService.getAllProducts(pageRequestDTO));
    }
    @GetMapping("/detail_dev")
    public void detail_dev(){

    }
    @GetMapping("/modify_dev")
    public void modify_dev(@RequestParam Long manuCode, PageRequestDTO pageRequestDTO, Model model){
        pageRequestDTO.setSize((int)materialRepository.count());
        model.addAttribute("material_list", materialService.findMaterialAll(pageRequestDTO));
        model.addAttribute("product", productService.getProductById(manuCode));
        model.addAttribute("structure_list", structureService.findByProduct_ManuCode(manuCode));
    }
    @PostMapping("/saveProduct")
    public Long saveProduct(@RequestBody SaveProductDTO saveProductDTO){
        ProductDTO productDTO=ProductDTO.builder()
                .manuCode((saveProductDTO.getManuCode()!=null)? saveProductDTO.getManuCode():null)
                .name(saveProductDTO.getProductName())
                .color(saveProductDTO.getProColor())
                .text(saveProductDTO.getProText())
                .uuid(saveProductDTO.getProFile())
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
        ProductDTO savedProductDTO=productService.addProduct(productDTO);

        List<StructureDTO> stlist=null;
        if(saveProductDTO.getManuCode()!=null){
            stlist=new ArrayList<>(structureService.findByProduct_ManuCode(saveProductDTO.getManuCode()));
            if(stlist.isEmpty()) stlist=null;
        }
        List<MaterQuantityDTO> quantityList=saveProductDTO.getMaterQuantityDTOs();
        if(quantityList.isEmpty()) quantityList=null;

        if(stlist==null && quantityList!=null){
            // 새로운 structure 저장
            quantityList.forEach(x->addStructure(savedProductDTO, x, x.getSno()));
        }else if(stlist!=null && quantityList==null){
            // 기존 structure 삭제
            stlist.forEach(x->structureService.deleteById(x.getSno()));
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
                stlist.forEach(x->structureService.deleteById(x.getSno()));
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
                    if(stlist!=null) stlist.forEach(x->structureService.deleteById(x.getSno()));
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

        return saveProductDTO.getSel();
    }
    private void addStructure(ProductDTO productDTO, MaterQuantityDTO materQuantityDTO, Long sno){
        MaterialDTO materialDTO=materialService.findM(materQuantityDTO.getCode());
        StructureDTO structureDTO=StructureDTO.builder()
                .sno(sno)
                .productDTO(productDTO)
                .materialDTO(materialDTO)
                .quantity(materQuantityDTO.getQuantity())
                .build();
        structureService.save(structureDTO);
    }

    @GetMapping("/delete_product")
    public String delete_product(@RequestParam(required = false) Long manuCode){
        List<StructureDTO> structureDTOList=structureService.findByProduct_ManuCode(manuCode);
        structureDTOList.forEach(x->structureService.deleteById(x.getSno()));
        productService.deleteProduct(manuCode);
        return "redirect:/development/list_dev";
    }
}
