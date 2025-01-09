package mit.iwrcore.IWRCore.controller_CRUD;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.FileContract;
import mit.iwrcore.IWRCore.entity.FileProPlan;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.SaveBaljuDTO;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.SaveGumsuDTO;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.SaveJodalChasuDTO;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthMemberDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.AttachFileDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileContractDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileProPlanDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.LLLSDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanSturctureDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Log4j2
public class CRUDProPlanToGumsuController {
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
    @Autowired
    private JodalChasuService jodalChasuService;
    @Autowired
    private StructureService structureService;
    @Autowired
    private LineStructureService lineStructureService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private BaljuService baljuService;
    @Autowired
    private BaljuChasuService baljuChasuService;
    @Autowired
    private GumsuService gumsuService;
    @Autowired
    private GumsuChasuService gumsuChasuService;
    @Autowired
    private ShipmentService shipmentService;


    // 생산계획> 라인-생산량 가져오기
    @GetMapping("/getLines")
    public List<PlanDTO> getLines(@RequestParam(required = false) Long manuCode){
        return planService.getLineByProduct(manuCode);
    }
    // 생산계획> 라인-생산량 저장
    @GetMapping("/saveLine")
    public List<Long> saveLine(@RequestParam(required = false) Long manuCode,
                               @RequestParam(required = false) List<Long> quantityList){
        ProductDTO productDTO=productService.getProduct(manuCode);
        List<PlanDTO> planDTOs=planService.getLineByProduct(manuCode);

        List<LineDTO> lines=lineService.getLines();
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
    // 생산계획> 생산계획 저장
    @PostMapping("/savePlan")
    public void savePlan(@ModelAttribute ProplanDTO proplanDTO,
                         @RequestParam(name = "manuCode", required = false) Long manuCode,
                         @RequestParam(name = "startD", required = false) String startD,
                         @RequestParam(name = "endD", required = false) String endD,
                         @RequestParam(name = "lineList", required = false) List<Long> lineList,
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
        proplanDTO.setFinCheck(0L);

        List<FileProPlanDTO> exDtoFileList=fileService.getProPlanFileList(proplanDTO.getProplanNo());
        List<FileProPlan> exEntityFileList=new ArrayList<>();
        if(exDtoFileList!=null) exDtoFileList.forEach(x->exEntityFileList.add(fileService.ppFile_dTe(x)));

        ProplanDTO savedProplanDTO=proplanService.saveProPlan(proplanDTO, exEntityFileList);
        Long proplanNo=savedProplanDTO.getProplanNo();

        saveLineStructure(savedProplanDTO, lineList);

        if(deleteFile!=null) fileService.deleteFileRun(deleteFile, "pp");
        if(files!=null) fileService.saveFileRun(files, proplanNo, "pp");

        if(proplanDTO.getProplanNo()==null) jodalPlanService.saveJodalPlanFromProplan(savedProplanDTO, memberDTO);

        MemberDTO manager=memberService.findMemberDto(1L, null);
        if(manager.getAutoJodalChasu()==1L) autoSaveJodalChasu(proplanNo);
    }
    // 생산계획> 생산계획-생산라인 저장
    private void saveLineStructure(ProplanDTO savedProplanDTO, List<Long> lineList){
        List<LineStructureDTO> exLineStructureList=lineStructureService.getLineStructureByProPlanNo(savedProplanDTO.getProplanNo());

        if(exLineStructureList.size()==0){
            lineList.forEach(x->{
                LineDTO lineDTO=lineService.getOneLine(x);
                LineStructureDTO lineStructureDTO=LineStructureDTO.builder()
                        .lineDTO(lineDTO)
                        .proplanDTO(savedProplanDTO)
                        .build();
                lineStructureService.saveLineStructure(lineStructureDTO);
            });
        }else{
            if(exLineStructureList.size()>=lineList.size()) {
                List<Long> noneSavedLong=new ArrayList<>();
                exLineStructureList.forEach(x->noneSavedLong.add(x.getLsNo()));
                for (int i = 0; i < lineList.size(); i++) {
                    LineDTO lineDTO = lineService.getOneLine(lineList.get(i));
                    LineStructureDTO lineStructureDTO = LineStructureDTO.builder()
                            .lsNo(exLineStructureList.get(i).getLsNo())
                            .lineDTO(lineDTO)
                            .proplanDTO(savedProplanDTO)
                            .build();
                    lineStructureService.saveLineStructure(lineStructureDTO);
                    noneSavedLong.remove(exLineStructureList.get(i).getLsNo());
                }
                if(noneSavedLong.size()!=0) noneSavedLong.forEach(x->lineStructureService.deleteStructure(x));
            }else{
                List<Long> noneSavedLong=new ArrayList<>();
                lineList.forEach(x->noneSavedLong.add(x));
                for (int i = 0; i < exLineStructureList.size(); i++) {
                    LineDTO lineDTO = lineService.getOneLine(lineList.get(i));
                    LineStructureDTO lineStructureDTO = LineStructureDTO.builder()
                            .lsNo(exLineStructureList.get(i).getLsNo())
                            .lineDTO(lineDTO)
                            .proplanDTO(savedProplanDTO)
                            .build();
                    lineStructureService.saveLineStructure(lineStructureDTO);
                    noneSavedLong.remove(lineList.get(i));
                }
                if(noneSavedLong.size()!=0){
                    noneSavedLong.forEach(x->{
                        LineDTO lineDTO=lineService.getOneLine(x);
                        LineStructureDTO lineStructureDTO=LineStructureDTO.builder()
                                .lineDTO(lineDTO)
                                .proplanDTO(savedProplanDTO)
                                .build();
                        lineStructureService.saveLineStructure(lineStructureDTO);
                    });
                }
            }
        }
    }
    // 생산계획> 조달차수 자동 저장
    private void autoSaveJodalChasu(Long proplanNo){
        List<ProPlanSturctureDTO> proPlanSturctureDTOList=jodalPlanService.getStructureStock(proplanNo);
        List<SaveJodalChasuDTO> saveJodalChasuDTOList=new ArrayList<>();

        for(ProPlanSturctureDTO ppsDTO:proPlanSturctureDTOList){
            ProductDTO productDTO=ppsDTO.getProplanDTO().getProductDTO();
            MaterialDTO materialDTO=ppsDTO.getStructureDTO().getMaterialDTO();
            JodalPlanDTO jodalPlanDTO=jodalPlanService.getJodalPlanByProductMaterial(productDTO.getManuCode(), materialDTO.getMaterCode()).get(0);
            StructureDTO structureDTO=structureService.getStructureByProductMaterial(productDTO.getManuCode(), materialDTO.getMaterCode()).get(0);

            Long calNum=ppsDTO.getProplanDTO().getPronum();
            Long stNum=structureDTO.getQuantity();
            Long stockNum=ppsDTO.getSumShip()-ppsDTO.getSumRequest();
            Long reqNum=(calNum*stNum)-stockNum;
            Long eachQuantity=(long) Math.floor(reqNum/3);
            Long remainder=reqNum%3;
            Long thirdQuantity=eachQuantity+remainder;

            LocalDateTime baseDate=ppsDTO.getProplanDTO().getStartDate().minusDays(3L);
            LocalDateTime firstDate=baseDate.plusDays(15L);
            LocalDateTime secondDate=firstDate.plusDays(15L);
            LocalDateTime thiredDate=secondDate.plusDays(15L);

            SaveJodalChasuDTO saveDTO= SaveJodalChasuDTO.builder()
                    .id(jodalPlanDTO.getJoNo()+"")
                    .oneNum(eachQuantity+"").twoNum(eachQuantity+"").threeNum(thirdQuantity+"")
                    .oneDate(firstDate.toString().split("T")[0])
                    .twoDate(secondDate.toString().split("T")[0])
                    .threeDate(thiredDate.toString().split("T")[0])
                    .build();
            saveJodalChasuDTOList.add(saveDTO);
        }
        saveJodalChasuDetail(saveJodalChasuDTOList);
    }
    // 생산계획> 생산계획 삭제
    @GetMapping("/deleteProPlan")
    public void deleteProPlan(@RequestParam(required = false) Long proplanNo){
        List<LineStructureDTO> lineStructureDTOs=lineStructureService.getLineStructureByProPlanNo(proplanNo);
        lineStructureDTOs.forEach(x->lineStructureService.deleteStructure(x.getLsNo()));

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



    // 조달계획> 조달계획 저장
    @PostMapping("/saveJodalChasu")
    public void saveJodalChasu(@RequestBody List<SaveJodalChasuDTO> list){
        saveJodalChasuDetail(list);
    }
    private void saveJodalChasuDetail(List<SaveJodalChasuDTO> list){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO = memberService.findMemberDto(authMemberDTO.getMno(), null);

        for(SaveJodalChasuDTO dto:list){
            JodalPlanDTO jodalPlanDTO=jodalPlanService.getJodalPlan(Long.valueOf(dto.getId()));

            List<JodalChasuDTO> jodalChasuDTOs=jodalChasuService.getJodalChasuByJodalPlan(jodalPlanDTO.getJoNo());

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
    // 조달계획> 조달계획 삭제
    @GetMapping("/deleteJodalChasu")
    public void deleteJodalChasu(@RequestParam(required = false) Long joNo){
        jodalChasuService.deleteJodalChasuByPlan(joNo);
    }



    // 계약> 계약 저장
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

        List<Long> conNoList=new ArrayList<>();
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
                conNoList.add(savedContract.getConNo());

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
        if(memberDTO.getAutoBaljuChasu()==1L) autoSaveBalju(conNoList);
    }
    // 계약> 발주 자동 저장
    private void autoSaveBalju(List<Long> conNoList){
        List<SaveBaljuDTO> saveBaljuDTOList=new ArrayList<>();

        for(Long conNo:conNoList){
            ContractDTO contractDTO=contractService.getContract(conNo);
            BaljuDTO baljuDTO=baljuService.getBaljuByContract(conNo);
            BaljuDTO baljuDTO2=baljuService.getRecentBaljuByMaterial(contractDTO.getJodalPlanDTO().getMaterialDTO().getMaterCode());
            PartnerDTO partnerDTO=partnerService.findPartnerDto(1L, null, null);
            List<JodalChasuDTO> jodalChasuDTOList=jodalChasuService.getJodalChasuByJodalPlan(contractDTO.getJodalPlanDTO().getJoNo());

            Long conNum=contractDTO.getConNum();
            Long eachQuantity=(long) Math.floor(conNum/3);
            Long remainder=conNum%3;
            Long thirdQuantity=eachQuantity+remainder;

            SaveBaljuDTO saveDTO=SaveBaljuDTO.builder()
                    .conNo(contractDTO.getConNo())
                    .baljuNo((baljuDTO!=null)?baljuDTO.getBaljuNo():null)
                    .baljuWhere((baljuDTO2!=null)?baljuDTO2.getBaljuWhere():partnerDTO.getLocation())
                    .oneNum(eachQuantity)
                    .twoNum(eachQuantity)
                    .threeNum(thirdQuantity)
                    .oneDate(jodalChasuDTOList.get(0).getJoDate().toString().split("T")[0])
                    .twoDate(jodalChasuDTOList.get(1).getJoDate().toString().split("T")[0])
                    .threeDate(jodalChasuDTOList.get(2).getJoDate().toString().split("T")[0])
                    .build();
            saveBaljuDTOList.add(saveDTO);
        }
        if(saveBaljuDTOList.size()!=0) saveBaljuDetail(saveBaljuDTOList);
    }
    // 계약> 계약 삭제
    @GetMapping("/deleteContract")
    public void deleteContract(@RequestParam(required = false) Long conNo){
        BaljuDTO baljuDTO=baljuService.getBaljuByContract(conNo);
        if(baljuDTO!=null) deleteBaljuDetail(baljuDTO.getBaljuNo());
        List<FileContractDTO> fileList=fileService.getContractFileList(conNo);
        List<String> deleteFile=new ArrayList<>();
        fileList.forEach(x->deleteFile.add(x.getUuid()));
        fileService.deleteFileRun(deleteFile, "c");
        contractService.deleteContract(conNo);
    }



    // 발주> 발주 저장
    @PostMapping("/saveBalju")
    @ResponseBody
    public void saveBalju(@RequestBody List<SaveBaljuDTO> baljuDataList){
        saveBaljuDetail(baljuDataList);
    }
    private void saveBaljuDetail(List<SaveBaljuDTO> baljuDataList){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO=(AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO=memberService.findMemberDto(authMemberDTO.getMno(), null);

        List<Long> baljuNoList=new ArrayList<>();
        for(SaveBaljuDTO preBaljuDTO:baljuDataList){
            ContractDTO contractDTO=contractService.getContract(preBaljuDTO.getConNo());
            Long sum=preBaljuDTO.getOneNum()+preBaljuDTO.getTwoNum()+preBaljuDTO.getThreeNum();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime oneDay=LocalDateTime.parse(preBaljuDTO.getOneDate()+" 00:00:00", formatter);
            LocalDateTime twoDay=LocalDateTime.parse(preBaljuDTO.getTwoDate()+" 00:00:00", formatter);
            LocalDateTime threeDay=LocalDateTime.parse(preBaljuDTO.getThreeDate()+" 00:00:00", formatter);

            List<LocalDateTime> timeList=new ArrayList<>(Arrays.asList(oneDay, twoDay, threeDay));
            List<Long> numList=new ArrayList<>(Arrays.asList(preBaljuDTO.getOneNum(), preBaljuDTO.getTwoNum(), preBaljuDTO.getThreeNum()));

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
                BaljuDTO savedBaljuDTO=baljuService.saveBalju(baljuDTO);
                baljuNoList.add(savedBaljuDTO.getBaljuNo());
                for(int i=0; i<3; i++){
                    BaljuChasuDTO baljuChasuDTO=BaljuChasuDTO.builder()
                            .balNum(numList.get(i))
                            .balDate(timeList.get(i))
                            .baljuDTO(savedBaljuDTO)
                            .build();
                    baljuChasuService.saveBaljuChasu(baljuChasuDTO);
                }
            }else{
                BaljuDTO baljuDTO=baljuService.getBalju(preBaljuDTO.getBaljuNo());
                baljuDTO.setBaljuNum(numList.stream().mapToLong(Long::longValue).sum());
                baljuDTO.setBaljuDate(oneDay);
                baljuDTO.setBaljuWhere(preBaljuDTO.getBaljuWhere());
                baljuDTO.setBaljuPlz(preBaljuDTO.getBaljuPlz());
                BaljuDTO savedBaljuDTO=baljuService.modifyBalju(baljuDTO);
                baljuNoList.add(savedBaljuDTO.getBaljuNo());

                List<BaljuChasuDTO> baljuChasuDTOs=baljuChasuService.getBaljuChasuListByBaljuNo(savedBaljuDTO.getBaljuNo());
                for(int i=0; i<baljuChasuDTOs.size(); i++){
                    BaljuChasuDTO baljuChasuDTO=baljuChasuDTOs.get(i);
                    baljuChasuDTO.setBalNum(numList.get(i));
                    baljuChasuDTO.setBalDate(timeList.get(i));
                    baljuChasuService.modifyBalhuChasu(baljuChasuDTO);
                }
            }
        }
        if(memberDTO.getAutoGumsuChasu()==1L) autoSaveGumsu(baljuNoList);
    }
    private void autoSaveGumsu(List<Long> baljuNoList){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO=(AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO=memberService.findMemberDto(authMemberDTO.getMno(), null);

        List<SaveGumsuDTO> saveGumsuDTOList=new ArrayList<>();

        for(Long baljuNo:baljuNoList){
            BaljuDTO baljuDTO=baljuService.getBalju(baljuNo);
            GumsuDTO gumsuDTO=gumsuService.getGumsuByBalju(baljuNo);
            List<BaljuChasuDTO> baljuChasuDTOList=baljuChasuService.getBaljuChasuListByBaljuNo(baljuNo);

            LocalDateTime firstDate=baljuChasuDTOList.get(0).getBalDate().minusDays(3L);
            LocalDateTime secondDate=baljuChasuDTOList.get(1).getBalDate().minusDays(3L);
            LocalDateTime thiredDate=baljuChasuDTOList.get(2).getBalDate().minusDays(3L);

            SaveGumsuDTO saveDTO=SaveGumsuDTO.builder()
                    .gumsuNo((gumsuDTO!=null)?gumsuDTO.getGumsuNo():null)
                    .baljuNo(baljuNo)
                    .person(memberDTO.getName())
                    .partnerNo(baljuDTO.getContractDTO().getPartnerDTO().getPno())
                    .oneNum((long)Math.floor(baljuChasuDTOList.get(0).getBalNum()/2))
                    .twoNum((long)Math.floor(baljuChasuDTOList.get(1).getBalNum()/2))
                    .threeNum((long)Math.floor(baljuChasuDTOList.get(2).getBalNum()/2))
                    .oneDate(firstDate.toString().split("T")[0])
                    .twoDate(secondDate.toString().split("T")[0])
                    .threeDate(thiredDate.toString().split("T")[0])
                    .build();
            saveGumsuDTOList.add(saveDTO);
        }
        if(saveGumsuDTOList.size()!=0) saveGumsuDetail(saveGumsuDTOList);
    }
    // 발주> 발주 삭제
    @PostMapping("/deleteBalju")
    public Long deleteBalju(@RequestParam(required = false) Long baljuNo){
        return deleteBaljuDetail(baljuNo);
    }
    private Long deleteBaljuDetail(Long baljuNo){
        List<ShipmentDTO> shipmentDTOList=shipmentService.getShipmentByBalju(baljuNo);
        if(shipmentDTOList.size()==0) {
            GumsuDTO gumsuDTO=gumsuService.getGumsuByBalju(baljuNo);
            if(gumsuDTO!=null){
                List<GumsuChasuDTO> gumsuChasuDTOList=gumsuChasuService.getGumsuChasuByGumsu(gumsuDTO.getGumsuNo());
                for(GumsuChasuDTO chasuDTO:gumsuChasuDTOList) gumsuChasuService.deleteGumsuChasu(chasuDTO.getGcnum());
                gumsuService.deleteGumsu(gumsuDTO.getGumsuNo());
            }
            List<BaljuChasuDTO> baljuChasuDTOList=baljuChasuService.getBaljuChasuListByBaljuNo(baljuNo);
            for(BaljuChasuDTO chasuDTO:baljuChasuDTOList) baljuChasuService.deleteBaljuChasu(chasuDTO.getBalNo());
            baljuService.deleteBalju(baljuNo);
            return 0L; // 삭제
        }
        else{
            return 1L; // 배송 존재로 삭제 불가능
        }
    }



    // 검수> 검수 저장
    @PostMapping("/saveGumsu")
    @ResponseBody
    public void saveGumsu(@RequestBody List<SaveGumsuDTO> list){
        saveGumsuDetail(list);
    }
    private void saveGumsuDetail(List<SaveGumsuDTO> list){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDTO authMemberDTO=(AuthMemberDTO) authentication.getPrincipal();
        MemberDTO memberDTO=memberService.findMemberDto(authMemberDTO.getMno(), null);

        for(SaveGumsuDTO preGumsuDTO:list){
            BaljuDTO baljuDTO=baljuService.getBalju(preGumsuDTO.getBaljuNo());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime oneDay=LocalDateTime.parse(preGumsuDTO.getOneDate()+" 00:00:00", formatter);
            LocalDateTime twoDay=LocalDateTime.parse(preGumsuDTO.getTwoDate()+" 00:00:00", formatter);
            LocalDateTime threeDay=LocalDateTime.parse(preGumsuDTO.getThreeDate()+" 00:00:00", formatter);

            List<LocalDateTime> timeList=new ArrayList<>(Arrays.asList(oneDay, twoDay, threeDay));
            List<Long> numList=new ArrayList<>(Arrays.asList(preGumsuDTO.getOneNum(), preGumsuDTO.getTwoNum(), preGumsuDTO.getThreeNum()));

            if(preGumsuDTO.getGumsuNo()==null) {
                GumsuDTO gumsuDTO = GumsuDTO.builder()
                        .who(preGumsuDTO.getPerson())
                        .make(0L)
                        .memberDTO(memberDTO)
                        .baljuDTO(baljuDTO)
                        .build();
                GumsuDTO savedGumsuDTO=gumsuService.saveGumsu(gumsuDTO);
                for(int i=0; i<3; i++){
                    GumsuChasuDTO gumsuChasuDTO=GumsuChasuDTO.builder()
                            .gumsuNum(numList.get(i))
                            .gumsuDate(timeList.get(i))
                            .gumsuDTO(savedGumsuDTO)
                            .memberDTO(memberDTO)
                            .build();
                    gumsuChasuService.saveGumsuChasu(gumsuChasuDTO);
                }
            }else{
                List<GumsuChasuDTO> gumsuChasuDTOs=gumsuChasuService.getGumsuChasuByGumsu(preGumsuDTO.getGumsuNo());
                for(int i=0; i<gumsuChasuDTOs.size(); i++){
                    GumsuChasuDTO gumsuChasuDTO=gumsuChasuDTOs.get(i);
                    gumsuChasuDTO.setGumsuNum(numList.get(i));
                    gumsuChasuDTO.setGumsuDate(timeList.get(i));
                    gumsuChasuService.modifyGumsuChasu(gumsuChasuDTO);
                }
            }
        }
    }
}
