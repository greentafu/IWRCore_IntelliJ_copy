package mit.iwrcore.IWRCore.controller_rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.AjaxDTO.NoneGumsuDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterCodeListDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartCodeListDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProCodeListDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterSDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartSDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProSDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.*;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/select")
@Log4j2
@RequiredArgsConstructor
public class SelectboxController {
    private final PartCodeService partCodeService;
    private final MaterService materService;
    private final ProCodeService proCodeService;
    private final ShipmentService shipmentService;
    private final PartnerService partnerService;
    private final GumsuService gumsuService;
    private final JodalPlanService jodalPlanService;
    private final MaterialService materialService;
    private final StructureService structureService;
    private final ProplanService proplanService;
    private final GumsuChasuService gumsuChasuService;
    private final ContractService contractService;
    private final LocationService locationService;
    private final BaljuService baljuService;
    private final ReturnsService returnsService;
    private final EmergencyService emergencyService;
    private final BaljuChasuService baljuChasuService;
    private final ProductService productService;
    private final LineStructureService lineStructureService;
    private final RequestService requestService;

    // 협력회사 카테고리 목록
    @GetMapping("/getPart")
    public PartCodeListDTO getPart(
            @RequestParam(required = false) Long lcode, @RequestParam(required = false) Long mcode,
            @RequestParam(required = false) Long scode, @RequestParam(required = false) Long type){
        PartLDTO ldto=(lcode!=null)?partCodeService.findPartL(lcode):null;
        PartMDTO mdto=(mcode!=null)?partCodeService.findPartM(mcode):null;
        PartSDTO sdto=(scode!=null)?partCodeService.findPartS(scode):null;
        PartCodeListDTO list=partCodeService.findListPartAll(ldto, mdto, sdto, type);

        if(scode!=null){
            list.setL(partCodeService.findPartS(scode).getPartMDTO().getPartLDTO().getPartLcode());
            list.setM(partCodeService.findPartS(scode).getPartMDTO().getPartMcode());
            list.setS(scode);
        }else if(mcode!=null){
            list.setL(partCodeService.findPartM(mcode).getPartLDTO().getPartLcode());
            list.setM(mcode);
            list.setS(null);
        }else if(lcode!=null){
            list.setL(lcode);
            list.setM(null);
            list.setS(null);
        }
        return list;
    }
    // 제품 카테고리 목록
    @GetMapping("/getPro")
    public ProCodeListDTO getPro(
            @RequestParam(required = false) Long lcode, @RequestParam(required = false) Long mcode,
            @RequestParam(required = false) Long scode, @RequestParam(required = false) Long type){
        ProLDTO ldto=(lcode!=null)?proCodeService.findProL(lcode):null;
        ProMDTO mdto=(mcode!=null)?proCodeService.findProM(mcode):null;
        ProSDTO sdto=(scode!=null)?proCodeService.findProS(scode):null;
        ProCodeListDTO list=proCodeService.findListProAll(ldto, mdto, sdto, type);

        if(scode!=null){
            list.setL(proCodeService.findProS(scode).getProMDTO().getProLDTO().getProLcode());
            list.setM(proCodeService.findProS(scode).getProMDTO().getProMcode());
            list.setS(scode);
        }else if(mcode!=null){
            list.setL(proCodeService.findProM(mcode).getProLDTO().getProLcode());
            list.setM(mcode);
            list.setS(null);
        }else if(lcode!=null){
            list.setL(lcode);
            list.setM(null);
            list.setS(null);
        }
        return list;
    }
    // 자재 카테고리 목록
    @GetMapping("/getMater")
    public MaterCodeListDTO getMater(
            @RequestParam(required = false) Long lcode, @RequestParam(required = false) Long mcode,
            @RequestParam(required = false) Long scode, @RequestParam(required = false) Long type){
        MaterLDTO ldto=(lcode!=null)?materService.findMaterL(lcode):null;
        MaterMDTO mdto=(mcode!=null)?materService.findMaterM(mcode):null;
        MaterSDTO sdto=(scode!=null)?materService.findMaterS(scode):null;
        MaterCodeListDTO list=materService.findListMaterAll(ldto, mdto, sdto, type);

        if(scode!=null){
            list.setL(materService.findMaterS(scode).getMaterMDTO().getMaterLDTO().getMaterLcode());
            list.setM(materService.findMaterS(scode).getMaterMDTO().getMaterMcode());
            list.setS(scode);
        }else if(mcode!=null){
            list.setL(materService.findMaterM(mcode).getMaterLDTO().getMaterLcode());
            list.setM(mcode);
            list.setS(null);
        }else if(lcode!=null){
            list.setL(lcode);
            list.setM(null);
            list.setS(null);
        }
        return list;
    }




    @GetMapping("/anyPartner")
    public PartnerDTO anyPartner(@RequestParam(required = false) Long pno){
        return partnerService.findPartnerDto(pno, null, null);
    }

    @GetMapping("/noneContractJodalPlan")
    public List<JodalPlanJodalChsuDTO> noneContractJodalPlan(){
        return jodalPlanService.noneContract();
    }

    @GetMapping("/materialList")
    public PageResultDTO<MaterialDTO, Material> materialList(
            @RequestParam(required = false) int page, @RequestParam(required = false) List<Long> longList,
            @RequestParam(required = false) Long selectMaterL, @RequestParam(required = false) Long selectMaterM,
            @RequestParam(required = false) Long selectMaterS, @RequestParam(required = false) String materialSearch){
        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .materL(selectMaterL).materM(selectMaterM).materS(selectMaterS).materialSearch(materialSearch)
                .materials(longList).build();
        return materialService.productMaterialList(requestDTO);
    }

    @GetMapping("/selectedController")
    public List<StructureDTO> selectedController(@RequestParam(required = false) String manuCode){
        Long code=Long.valueOf(manuCode);
        List<StructureDTO> dtoList=structureService.getStructureByProduct(code);
        return dtoList;
    }

    @GetMapping("/checkProPlan")
    public Long checkProPlan(@RequestParam(required = false) Long manuCode){
        return proplanService.checkProPlan(manuCode);
    }


    // 생산계획> 모든 제품 목록 가져오기
    @PostMapping("/getAllProduct")
    public PageResultDTO<ProductDTO, Product> getAllProduct(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) Long selectProL, @RequestParam(required = false) Long selectProM,
            @RequestParam(required = false) Long selectProS, @RequestParam(required = false) String productSearch){
        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .build();
        return productService.getCheckProducts(requestDTO);
    }

    // 계약서> 모든 협력회사 목록 가져오기
    @PostMapping("/getAllPartner")
    public PageResultDTO<PartnerDTO, Partner> getAllPartner(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) Long selectPartL, @RequestParam(required = false) Long selectPartM,
            @RequestParam(required = false) Long selectPartS, @RequestParam(required = false) String partnerSearch){
        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .partL(selectPartL).partM(selectPartM).partS(selectPartS).partnerSearch(partnerSearch)
                .build();
        return partnerService.getAllPartner(requestDTO);
    }
    // 계약서> 계약하지 않은 조달계획 목록 가져오기
    @PostMapping("/getAllNonContractJodalPlan")
    public PageResultDTO<JodalPlanJodalChsuDTO, Object[]> getAllNonContractJodalPlan(
            @RequestParam(required = false) int page2, @RequestParam(required = false) List<Long> longList,
            @RequestParam(required = false) Long selectMaterL2, @RequestParam(required = false) Long selectMaterM2,
            @RequestParam(required = false) Long selectMaterS2, @RequestParam(required = false) String materialSearch2){
        PageRequestDTO2 requestDTO2=PageRequestDTO2.builder()
                .page2(page2).size2(15)
                .materL2(selectMaterL2).materM2(selectMaterM2).materS2(selectMaterS2).materialSearch2(materialSearch2)
                .jodalPlans(longList)
                .build();
        return jodalPlanService.noneContractJodalPlan(requestDTO2);
    }
    // 계약서> 계약하지 않은 조달계획 가져오기
    @PostMapping("/getOneNonContractJodalPlan")
    public JodalPlanJodalChsuDTO getOneNonContractJodalPlan(@RequestParam(required = false) Long joNo){
        return jodalPlanService.selectedJodalPlan(joNo);
    }
    // 계약서> 협력회사 선택
    @PostMapping("/getOnePartner")
    public PartnerDTO getOnePartner(@RequestParam(required = false) Long pno){
        return partnerService.findPartnerDto(pno, null, null);
    }


    // 발주서> 발주 가능한 협력회사 목록 가져오기
    @PostMapping("/getBaljuPartner")
    public PageResultDTO<PartnerDTO, Partner> getBaljuPartner(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) Long selectPartL, @RequestParam(required = false) Long selectPartM,
            @RequestParam(required = false) Long selectPartS, @RequestParam(required = false) String partnerSearch){
        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .partL(selectPartL).partM(selectPartM).partS(selectPartS).partnerSearch(partnerSearch)
                .build();
        return partnerService.getBaljuPartner(requestDTO);
    }
    // 발주서> 회사에 알맞은 발주하지 않은 계약 목록
    @PostMapping("/getNonBaljuContract")
    public List<NewOrderDTO> getNonBaljuContract(@RequestParam(required = false) Long pno){
        List<NewOrderDTO> result=null;
        if(pno!=null) result=contractService.newOrderContract(pno);
        return result;
    }
    // 발주서> 배송지 목록
    @PostMapping("/getLocation")
    public PageResultDTO<LocationDTO, Location> getLocation(
            @RequestParam(required = false) int page2, @RequestParam(required = false) String word){
        PageRequestDTO2 requestDTO2=PageRequestDTO2.builder()
                .page2(page2).size2(15)
                .loc((word!="")?word:null)
                .build();
        return locationService.getLocationList(requestDTO2);
    }


    // 검수차수> 검수차수 설정 가능한 협력회사 목록 가져오기
    @PostMapping("/getGumsuPartner")
    public PageResultDTO<PartnerDTO, Partner> getGumsuPartner(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) Long selectPartL, @RequestParam(required = false) Long selectPartM,
            @RequestParam(required = false) Long selectPartS, @RequestParam(required = false) String partnerSearch){
        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .partL(selectPartL).partM(selectPartM).partS(selectPartS).partnerSearch(partnerSearch)
                .build();
        return partnerService.getGumsuPartner(requestDTO);
    }
    // 검수차수> 회사에 알맞은 검수차수 없는 발주 목록
    @PostMapping("/getNonGumsuChasu")
    public List<NoneGumsuDTO> getNonGumsuChasu(@RequestParam(required = false) Long pno){
        List<NoneGumsuDTO> result=new ArrayList<>();
        Set<BaljuDTO> baljuDTOSet=new HashSet<>();
        Set<BaljuChasuDTO> baljuChasuDTOSet=new HashSet<>();

        List<BaljuBaljuChasuDTO> baljuJodalChasuDTOList=gumsuService.getNoneGumsuBalju(pno);

        for(BaljuBaljuChasuDTO dto:baljuJodalChasuDTOList){
            baljuDTOSet.add(dto.getBaljuDTO());
            baljuChasuDTOSet.add(dto.getBaljuChasuDTO());
            if(baljuChasuDTOSet.size()==3){
                BaljuDTO baljuDTO=baljuDTOSet.stream().toList().get(0);
                List<BaljuChasuDTO> baljuChasuDTOList=baljuChasuDTOSet.stream().toList();
                List<BaljuChasuDTO> sortedBC=baljuChasuDTOList.stream().sorted(Comparator.comparing(BaljuChasuDTO::getBalNo)).toList();
                NoneGumsuDTO noneGumsuDTO= NoneGumsuDTO.builder().baljuDTO(baljuDTO).baljuChasuDTOs(sortedBC).build();
                result.add(noneGumsuDTO);
                baljuDTOSet.clear();
                baljuChasuDTOSet.clear();
            }
        }
        return result;
    }


    // 협력회사> 자재 선택
    @PostMapping("/getPartnerProduct")
    public PartnerMainDTO getPartnerProduct(@RequestParam(required = false) Long baljuNo){
        BaljuDTO baljuDTO=baljuService.getBalju(baljuNo);

        PartnerMainDTO partnerMainDTO=PartnerMainDTO.builder().baljuDTO(baljuDTO).build();

        List<ShipmentDTO> shipmentDTOs=shipmentService.getShipmentByBalju(baljuNo);
        List<ReturnsDTO> returnsDTOs=returnsService.getReturnsList(baljuDTO.getBaljuNo());
        List<EmergencyDTO> emergencyDTOs=emergencyService.getEmergencyByBalju(baljuDTO.getBaljuNo());

        Long totalShipment=0L;
        Long totalReturn=0L;
        if(shipmentDTOs!=null){
            for(ShipmentDTO shipmentDTO:shipmentDTOs) totalShipment+=shipmentDTO.getShipNum();
        }
        if(returnsDTOs!=null) {
            for(ReturnsDTO returnsDTO:returnsDTOs){
                if(returnsDTO.getReturnCheck()==0) partnerMainDTO.setReturns(returnsDTO.getReNO());
                totalReturn+=returnsDTO.getShipmentDTO().getShipNum();
            }
        }
        if(emergencyDTOs!=null){
            for(EmergencyDTO emergencyDTO:emergencyDTOs){
                if(emergencyDTO.getEmcheck()==0) partnerMainDTO.setEmergency(emergencyDTO.getEmerNo());
            }
        }
        partnerMainDTO.setTotalShipment(totalShipment);
        partnerMainDTO.setTotalReturn(totalReturn);

        List<QuantityDateDTO> baljuList=baljuChasuService.partnerMainBalju(baljuNo, baljuDTO.getBaljuDate(), totalShipment-totalReturn);
        List<QuantityDateDTO> gumsuList=gumsuChasuService.partnerMainGumsu(baljuDTO.getBaljuNo());
        partnerMainDTO.setBaljuList(baljuList);
        partnerMainDTO.setGumsuList(gumsuList);

        return partnerMainDTO;
    }


    // 거래명세서> 거래명세서 발급 가능한 협력회사 목록 가져오기
    @PostMapping("/getInvoicePartner")
    public PageResultDTO<PartnerDTO, Partner> getInvoicePartner(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) Long selectPartL, @RequestParam(required = false) Long selectPartM,
            @RequestParam(required = false) Long selectPartS, @RequestParam(required = false) String partnerSearch){
        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .partL(selectPartL).partM(selectPartM).partS(selectPartS).partnerSearch(partnerSearch)
                .build();
        return partnerService.getInvoicePartner(requestDTO);
    }
    // 거래명세서> 협력회사별 거래명세서 발급 가능한 배송 목록 가져오기
    @PostMapping("/getInvoiceShipment")
    public PageResultDTO<ShipmentDTO, Shipment> getShipment(
            @RequestParam(required = false) Long pno, @RequestParam(required = false) int page2,
            @RequestParam(required = false) List<Long> longList, @RequestParam(required = false) Long tranNO,
            @RequestParam(required = false) Long selectMaterL2, @RequestParam(required = false) Long selectMaterM2,
            @RequestParam(required = false) Long selectMaterS2, @RequestParam(required = false) String materialSearch2){
        if(pno==null) return null;
        PageRequestDTO2 requestDTO2=PageRequestDTO2.builder()
                .page2(page2).size2(15)
                .materL2(selectMaterL2).materM2(selectMaterM2).materS2(selectMaterS2).materialSearch2(materialSearch2)
                .pno2(pno).shipments(longList).code(tranNO)
                .build();
        return shipmentService.couldInvoiceShipment(requestDTO2);
    }
    // 거래명세서> 거래명세서 없는 배송정보 가져오기
    @PostMapping("/getOneNonInvoiceShipment")
    public ShipmentDTO getOneNonInvoiceShipment(@RequestParam(required = false) Long shipNo){
        return shipmentService.getShipment(shipNo);
    }
    // 거래명세서> 거래명세서에 해당하는 배송목록 가져오기
    @PostMapping("/getModifyInvoiceShipment")
    public List<ShipmentDTO> getModifyInvoiceShipment(@RequestParam(required = false) Long tranNO){
        return shipmentService.getShipmentByInvoice(tranNO);
    }

    // 출하요청> 생산계획 존재하는 모든 제품목록 가져오기
    @PostMapping("/getPlanProduct")
    public PageResultDTO<ProplanDTO, ProPlan> getPlanProduct(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) Long selectProL, @RequestParam(required = false) Long selectProM,
            @RequestParam(required = false) Long selectProS, @RequestParam(required = false) String productSearch){
        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .build();
        return proplanService.getNotFinProPlan(requestDTO);
    }
    // 출하요청> 생산계획 하나 선택
    @PostMapping("/getOneProPlan")
    public List<ProPlanSturctureDTO> getOneProPlan(
            @RequestParam(required = false) Long proplanNo){
        return jodalPlanService.getStructureStock(proplanNo);
    }
    // 출하요청> 생산계획 수정
    @PostMapping("/getRequest")
    public List<PreRequestSturctureDTO> getRequest(
            @RequestParam(required = false) Long preCode){
        return requestService.getStructureStock(preCode);
    }
    // 출하요청> 생산계획 하나 선택시 라인목록
    @PostMapping("/getOneProPlanLine")
    public List<LineStructureDTO> getOneProPlanLine(
            @RequestParam(required = false) Long proplanNo){
        return lineStructureService.getLineStructureByProPlanNo(proplanNo);
    }


    // 재고목록> 재고 상세보기
    @PostMapping("/getStockDetail")
    public PageResultDTO<StockDetailDTO, Object[]> getStockDetail(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) Long materCode, @RequestParam(required = false) Integer selectedYear){
        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .materCode(materCode).selectedYear(selectedYear)
                .build();
        return contractService.stockdetailList(requestDTO);
    }

    // 긴급남품> 자재 선택
    @PostMapping("/getUrgencyContract")
    public PageResultDTO<EmergencyDTO, Emergency> getUrgencyContract(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) Long proNo, @RequestParam(required = false) Long materCode){
        Pageable pageable= PageRequest.of(page-1, 10, Sort.by("emerNo").descending());
        return emergencyService.getEmergencyByProMat(pageable, proNo, materCode);
    }
}
