package mit.iwrcore.IWRCore.controller_rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.*;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProSDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.*;
import mit.iwrcore.IWRCore.security.dto.multiDTO.LLLSDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Log4j2
public class CRUDController {
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MaterService materService;
    @Autowired
    private BoxService boxService;
    @Autowired
    private ProductService productService;
    @Autowired
    private StructureService structureService;
    @Autowired
    private ProCodeService proCodeService;
    @Autowired
    private FileService fileService;
    @Autowired
    private PlanService planService;
    @Autowired
    private LineService lineService;
    @Autowired
    private ProplanService proplanService;
    @Autowired
    private JodalPlanService jodalPlanService;
    @Autowired
    private JodalChasuService jodalChasuService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private BaljuService baljuService;
    @Autowired
    private BaljuChasuService baljuChasuService;

    // 직원

    // 협력회사

    // 카테고리(협력회사)

    // 카테고리(제품)

    // 카테고리(자재)

    // 창고

    // 배송지
    @PostMapping("/saveLocation")
    public void saveLocation(@RequestParam(name = "location") String location){
        LocationDTO dto=LocationDTO.builder().location(location).build();
        locationService.saveLocation(dto);
    }
    @GetMapping("/deleteLocation")
    public void deleteLocation(Long lno){}

    // 자재
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

    // 제품
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
            stlist=new ArrayList<>(structureService.findByProduct_ManuCode(saveProductDTO.getManuCode()));
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
        List<StructureDTO> structureDTOList=structureService.findByProduct_ManuCode(manuCode);
        structureDTOList.forEach(x->structureService.deleteStructure(x.getSno()));
        List<FileProductDTO> fileList=fileService.getProductFileList(manuCode);
        List<String> deleteFile=new ArrayList<>();
        fileList.forEach(x->deleteFile.add(x.getUuid()));
        fileService.deleteFileRun(deleteFile, "p");
        productService.deleteProduct(manuCode);
    }

    // 생산계획
    @GetMapping("/getLines")
    public List<PlanDTO> getLines(@RequestParam(required = false) Long manuCode){
        List<PlanDTO> planDTOs=planService.findByProductId(manuCode);
        return planDTOs;
    }
    @GetMapping("/saveLine")
    public List<Long> saveLine(@RequestParam(required = false) Long manuCode,
                               @RequestParam(required = false) List<Long> quantityList){
        ProductDTO productDTO=productService.getProduct(manuCode);
        List<PlanDTO> planDTOs=planService.findByProductId(manuCode);

        List<String> lines=lineService.getLines();
        for(int i=0; i<quantityList.size(); i++){
            Long tempCode=null;
            if(planDTOs!=null){
                PlanDTO planDTO=planService.findLineByLine(manuCode, lines.get(i));
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

        List<JodalPlanDTO> jodalPlanDTOs=jodalPlanService.findJodalPlanByProPlan(proplanNo);
        if(jodalPlanDTOs!=null) jodalPlanDTOs.forEach(x->jodalPlanService.deleteJodalPlan(x.getJoNo()));

        proplanService.deleteById(proplanNo);
    }

    // 조달차수
    @PostMapping("/saveJodalChasu")
    public void saveJodalChasu(@RequestBody List<SaveJodalChasuDTO> list){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO = memberService.findMemberDto(authMemberDTO.getMno(), null);

        for(SaveJodalChasuDTO dto:list){
            JodalPlanDTO jodalPlanDTO=jodalPlanService.getJodalPlan(Long.valueOf(dto.getId()));

            List<JodalChasuDTO> jodalChasuDTOs=jodalChasuService.findJCfromJP(jodalPlanDTO.getJoNo());

            List<Long> longs=new ArrayList<>();
            longs.add(Long.valueOf(dto.getOneNum()));
            longs.add(Long.valueOf(dto.getTwoNum()));
            longs.add(Long.valueOf(dto.getThreeNum()));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<LocalDateTime> localDateTimes=new ArrayList<>();
            localDateTimes.add(LocalDateTime.parse(dto.getOneDate()+" 00:00:00", formatter));
            localDateTimes.add(LocalDateTime.parse(dto.getTwoDate()+" 00:00:00", formatter));
            localDateTimes.add(LocalDateTime.parse(dto.getThreeDate()+" 00:00:00", formatter));

            for(int i=0; i<3; i++){
                JodalChasuDTO jodalChasuDTO=JodalChasuDTO.builder()
                        .jcnum((jodalChasuDTOs.size()!=0)?jodalChasuDTOs.get(i).getJcnum():null)
                        .jodalPlanDTO(jodalPlanDTO)
                        .joNum(longs.get(i))
                        .joDate(localDateTimes.get(i))
                        .memberDTO(memberDTO)
                        .build();
                jodalChasuService.saveJodalChasu(jodalChasuDTO);
            }
        }
    }
    @GetMapping("/deleteJodalChasu")
    public void deleteJodalChasu(@RequestParam(required = false) Long joNo){
        jodalChasuService.deleteJodalChasuByPlan(joNo);
    }

    // 계약서
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

    // 발주서
    @PostMapping("/saveBalju")
    @ResponseBody
    public void saveBalju(@RequestBody List<SaveBaljuDTO> baljuDataList){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO=(AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO=memberService.findMemberDto(authMemberDTO.getMno(), null);

        for(SaveBaljuDTO preBaljuDTO:baljuDataList){
            ContractDTO contractDTO=contractService.getContractById(preBaljuDTO.getConNo());
            Long sum=preBaljuDTO.getOneNum()+preBaljuDTO.getTwoNum()+preBaljuDTO.getThreeNum();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime oneDay=LocalDateTime.parse(preBaljuDTO.getOneDate()+" 00:00:00", formatter);

            BaljuDTO savedBaljuDTO=null;
            if(preBaljuDTO.getBaljuNo()==null){
                BaljuDTO baljuDTO=BaljuDTO.builder()
                        .baljuNo(preBaljuDTO.getBaljuNo())
                        .baljuNum(sum)
                        .baljuDate(oneDay)
                        .baljuWhere(preBaljuDTO.getBaljuWhere())
                        .baljuPlz(preBaljuDTO.getBaljuPlz())
                        .finCheck(0L)
                        .contractDTO(contractDTO)
                        .memberDTO(memberDTO).build();
                savedBaljuDTO=baljuService.saveBalju(baljuDTO, null);
                saveBaljuChasu(null, preBaljuDTO, savedBaljuDTO);
            }else{
                List<Long> idx=new ArrayList<>();
                List<BaljuChasu> baljuChasus=new ArrayList<>();
                List<BaljuChasuDTO> baljuChasuDTOs=baljuChasuService.getBaljuChasuListByBaljuNo(preBaljuDTO.getBaljuNo());
                baljuChasuDTOs.forEach(x->{
                    baljuChasus.add(baljuChasuService.dtoToEntity(x));
                    idx.add(x.getBalNo());
                    x.setBaljuDTO(null);
                    baljuChasuService.saveBaljuChasu(x);
                });
                BaljuDTO baljuDTO=baljuService.getBaljuById(preBaljuDTO.getBaljuNo());
                baljuDTO.setBaljuNum(sum);
                baljuDTO.setBaljuDate(oneDay);
                baljuDTO.setBaljuWhere(preBaljuDTO.getBaljuWhere());
                baljuDTO.setBaljuPlz(preBaljuDTO.getBaljuPlz());

                savedBaljuDTO=baljuService.saveBalju(baljuDTO, baljuChasus);
                saveBaljuChasu(idx, preBaljuDTO, savedBaljuDTO);
            }
        }
    }
    private void saveBaljuChasu(List<Long> idx, SaveBaljuDTO preBaljuDTO, BaljuDTO savedBaljuDTO){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime oneDay=LocalDateTime.parse(preBaljuDTO.getOneDate()+" 00:00:00", formatter);
        LocalDateTime twoDay=LocalDateTime.parse(preBaljuDTO.getTwoDate()+" 00:00:00", formatter);
        LocalDateTime threeDay=LocalDateTime.parse(preBaljuDTO.getThreeDate()+" 00:00:00", formatter);

        BaljuChasuDTO baljuChasuDTO1=BaljuChasuDTO.builder()
                .balNo((idx!=null)?idx.get(0):null)
                .balNum(preBaljuDTO.getOneNum())
                .balDate(oneDay)
                .baljuDTO(savedBaljuDTO)
                .build();
        baljuChasuService.saveBaljuChasu(baljuChasuDTO1);
        BaljuChasuDTO baljuChasuDTO2=BaljuChasuDTO.builder()
                .balNo((idx!=null)?idx.get(1):null)
                .balNum(preBaljuDTO.getTwoNum())
                .balDate(twoDay)
                .baljuDTO(savedBaljuDTO)
                .build();
        baljuChasuService.saveBaljuChasu(baljuChasuDTO2);
        BaljuChasuDTO baljuChasuDTO3=BaljuChasuDTO.builder()
                .balNo((idx!=null)?idx.get(2):null)
                .balNum(preBaljuDTO.getThreeNum())
                .balDate(threeDay)
                .baljuDTO(savedBaljuDTO)
                .build();
        baljuChasuService.saveBaljuChasu(baljuChasuDTO3);
    }
    @PostMapping("/deleteBalju")
    public void deleteBalju(@RequestParam(required = false) Long baljuNo){
        baljuService.deleteBalju(baljuNo);
    }
}
