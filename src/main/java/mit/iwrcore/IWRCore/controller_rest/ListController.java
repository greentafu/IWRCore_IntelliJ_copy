package mit.iwrcore.IWRCore.controller_rest;

import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.AuthDTO.AuthPartnerDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.*;
import mit.iwrcore.IWRCore.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/list")
@Log4j2
public class ListController {
    @Autowired
    private MaterialService materialService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProplanService proplanService;
    @Autowired
    private JodalPlanService jodalPlanService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private BaljuService baljuService;
    @Autowired
    private GumsuService gumsuService;
    @Autowired
    private GumsuChasuService gumsuChasuService;
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private ReturnsService returnsService;
    @Autowired
    private PreRequestService preRequestService;
    @Autowired
    private PartCodeService partCodeService;
    @Autowired
    private ProCodeService proCodeService;
    @Autowired
    private MaterService materService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private EmergencyService emergencyService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private BaljuChasuService baljuChasuService;

    @GetMapping("/materialList")
    public PageResultDTO<MaterialDTO, Material> materialList(@RequestParam(required = false) int page,
                                                             @RequestParam(required = false) Long selectProL,@RequestParam(required = false) Long selectProM,
                                                             @RequestParam(required = false) Long selectProS,@RequestParam(required = false) String productSearch,
                                                             @RequestParam(required = false) Long selectedBox,
                                                             @RequestParam(required = false) Long selectMaterL,@RequestParam(required = false) Long selectMaterM,
                                                             @RequestParam(required = false) Long selectMaterS,@RequestParam(required = false) String materialSearch){

        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }
        if (materialSearch != null && materialSearch.trim().isEmpty()) { materialSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .materL(selectMaterL).materM(selectMaterM).materS(selectMaterS).materialSearch(materialSearch).box(selectedBox).build();

        return materialService.getMaterialAll(requestDTO);
    }

    @GetMapping("/productDevList")
    public PageResultDTO<ProductDTO, Product> productDevList(@RequestParam(required = false) int page,
                                                             @RequestParam(required = false) Long selectProL,@RequestParam(required = false) Long selectProM,
                                                             @RequestParam(required = false) Long selectProS,@RequestParam(required = false) String productSearch,
                                                             @RequestParam(required = false) Long productImsiCheck){
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .productImsiCheck(productImsiCheck).build();

        return productService.getAllProducts(requestDTO);
    }

    @GetMapping("/nonCheckProductList")
    public PageResultDTO<ProductDTO, Product> nonCheckProductList(@RequestParam(required = false) int page,
                                                                  @RequestParam(required = false) Long selectProL,@RequestParam(required = false) Long selectProM,
                                                                  @RequestParam(required = false) Long selectProS,@RequestParam(required = false) String productSearch){
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch).build();

        return productService.getNonCheckProducts(requestDTO);
    }

    @GetMapping("/checkedProductList")
    public PageResultDTO<ProductDTO, Product> checkedProductList(@RequestParam(required = false) int page,
                                                             @RequestParam(required = false) Long selectProL,@RequestParam(required = false) Long selectProM,
                                                             @RequestParam(required = false) Long selectProS,@RequestParam(required = false) String productSearch){
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch).build();

        return productService.getCheckProducts(requestDTO);
    }

    @GetMapping("/nonPlanProductList")
    public PageResultDTO<ProductDTO, Product> nonPlanProductList(@RequestParam(required = false) int page,
                                                                 @RequestParam(required = false) Long selectProL,@RequestParam(required = false) Long selectProM,
                                                                 @RequestParam(required = false) Long selectProS,@RequestParam(required = false) String productSearch){
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(10)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch).build();

        return productService.getNonPlanProducts(requestDTO);
    }

    @GetMapping("/proPlanList")
    public PageResultDTO<ProPlanContractNumDTO, Object[]> proPlanList(@RequestParam(required = false) int page2,
                                                                      @RequestParam(required = false) Long selectProL2,@RequestParam(required = false) Long selectProM2,
                                                                      @RequestParam(required = false) Long selectProS2,@RequestParam(required = false) String productSearch2,
                                                                      @RequestParam(required = false) Long proplanProgress2){
        if (productSearch2 != null && productSearch2.trim().isEmpty()) { productSearch2 = null; }

        PageRequestDTO2 requestDTO=PageRequestDTO2.builder()
                .page2(page2).size2(15)
                .proL2(selectProL2).proM2(selectProM2).proS2(selectProS2).productSearch2(productSearch2)
                .proplanProgress2(proplanProgress2).build();

        return proplanService.getAllProPlans(requestDTO);
    }

    @GetMapping("/nonChasuJodalPlan")
    public PageResultDTO<JodalPlanDTO, JodalPlan> nonChasuJodalPlan(@RequestParam(required = false) int page,
                                                                    @RequestParam(required = false) Long selectProL, @RequestParam(required = false) Long selectProM,
                                                                    @RequestParam(required = false) Long selectProS, @RequestParam(required = false) String productSearch,
                                                                    @RequestParam(required = false) Long selectMaterL, @RequestParam(required = false) Long selectMaterM,
                                                                    @RequestParam(required = false) Long selectMaterS, @RequestParam(required = false) String materialSearch){

        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }
        if (materialSearch != null && materialSearch.trim().isEmpty()) { materialSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .materL(selectMaterL).materM(selectMaterM).materS(selectMaterS).materialSearch(materialSearch).build();

        return jodalPlanService.nonJodalplanMaterial2(requestDTO);
    }

    @GetMapping("/yesChasuJodalPlan")
    public PageResultDTO<ContractJodalChasuDTO, Object[]> yesChasuJodalPlan(@RequestParam(required = false) int page2,
                                                                            @RequestParam(required = false) Long selectProL2, @RequestParam(required = false) Long selectProM2,
                                                                            @RequestParam(required = false) Long selectProS2, @RequestParam(required = false) String productSearch2,
                                                                            @RequestParam(required = false) Long selectMaterL2, @RequestParam(required = false) Long selectMaterM2,
                                                                            @RequestParam(required = false) Long selectMaterS2, @RequestParam(required = false) String materialSearch2,
                                                                            @RequestParam(required = false) Long progressContract2){

        if (productSearch2 != null && productSearch2.trim().isEmpty()) { productSearch2 = null; }
        if (materialSearch2 != null && materialSearch2.trim().isEmpty()) { materialSearch2 = null; }

        PageRequestDTO2 requestDTO=PageRequestDTO2.builder()
                .page2(page2).size2(10)
                .proL2(selectProL2).proM2(selectProM2).proS2(selectProS2).productSearch2(productSearch2)
                .materL2(selectMaterL2).materM2(selectMaterM2).materS2(selectMaterS2).materialSearch2(materialSearch2)
                .progressContract2(progressContract2).build();

        return contractService.yesJodalplanMaterial(requestDTO);
    }

    @GetMapping("/nonContractJodalPlan")
    public PageResultDTO<ContractJodalChasuDTO, Object[]> nonContractJodalPlan(@RequestParam(required = false) int page,
                                                                            @RequestParam(required = false) Long selectProL, @RequestParam(required = false) Long selectProM,
                                                                            @RequestParam(required = false) Long selectProS, @RequestParam(required = false) String productSearch,
                                                                            @RequestParam(required = false) Long selectMaterL, @RequestParam(required = false) Long selectMaterM,
                                                                            @RequestParam(required = false) Long selectMaterS, @RequestParam(required = false) String materialSearch){

        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }
        if (materialSearch != null && materialSearch.trim().isEmpty()) { materialSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .materL(selectMaterL).materM(selectMaterM).materS(selectMaterS).materialSearch(materialSearch).build();

        return contractService.couldContractMaterial(requestDTO);
    }

    @GetMapping("/yesContractJodalPlan")
    public PageResultDTO<ContractBaljuDTO, Object[]> yesContractJodalPlan(@RequestParam(required = false) int page2,
                                                                          @RequestParam(required = false) Long selectPartL2, @RequestParam(required = false) Long selectPartM2,
                                                                          @RequestParam(required = false) Long selectPartS2, @RequestParam(required = false) String partnerSearch2,
                                                                          @RequestParam(required = false) Long selectProL2, @RequestParam(required = false) Long selectProM2,
                                                                          @RequestParam(required = false) Long selectProS2, @RequestParam(required = false) String productSearch2,
                                                                          @RequestParam(required = false) Long selectMaterL2, @RequestParam(required = false) Long selectMaterM2,
                                                                          @RequestParam(required = false) Long selectMaterS2, @RequestParam(required = false) String materialSearch2,
                                                                          @RequestParam(required = false) Long baljuProgress2){
        if (partnerSearch2 != null && partnerSearch2.trim().isEmpty()) { partnerSearch2 = null; }
        if (productSearch2 != null && productSearch2.trim().isEmpty()) { productSearch2 = null; }
        if (materialSearch2 != null && materialSearch2.trim().isEmpty()) { materialSearch2 = null; }

        PageRequestDTO2 requestDTO=PageRequestDTO2.builder()
                .page2(page2).size2(10)
                .partL2(selectPartL2).partM2(selectPartM2).partS2(selectPartS2).partnerSearch2(partnerSearch2)
                .proL2(selectProL2).proM2(selectProM2).proS2(selectProS2).productSearch2(productSearch2)
                .materL2(selectMaterL2).materM2(selectMaterM2).materS2(selectMaterS2).materialSearch2(materialSearch2)
                .baljuProgress2(baljuProgress2).build();

        return baljuService.finishedContract(requestDTO);
    }

    @GetMapping("/nonBaljuContract")
    public PageResultDTO<ContractBaljuDTO, Object[]> nonBaljuContract(@RequestParam(required = false) int page,
                                                                      @RequestParam(required = false) Long selectPartL, @RequestParam(required = false) Long selectPartM,
                                                                      @RequestParam(required = false) Long selectPartS, @RequestParam(required = false) String partnerSearch,
                                                                      @RequestParam(required = false) Long selectProL, @RequestParam(required = false) Long selectProM,
                                                                      @RequestParam(required = false) Long selectProS, @RequestParam(required = false) String productSearch,
                                                                      @RequestParam(required = false) Long selectMaterL, @RequestParam(required = false) Long selectMaterM,
                                                                      @RequestParam(required = false) Long selectMaterS, @RequestParam(required = false) String materialSearch){
        if (partnerSearch != null && partnerSearch.trim().isEmpty()) { partnerSearch = null; }
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }
        if (materialSearch != null && materialSearch.trim().isEmpty()) { materialSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .partL(selectPartL).partM(selectPartM).partS(selectPartS).partnerSearch(partnerSearch)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .materL(selectMaterL).materM(selectMaterM).materS(selectMaterS).materialSearch(materialSearch).build();

        return baljuService.couldBalju(requestDTO);
    }

    @GetMapping("/yesBaljuContract")
    public PageResultDTO<ContractBaljuDTO, Object[]> yesBaljuContract(@RequestParam(required = false) int page2,
                                                                      @RequestParam(required = false) Long selectPartL2, @RequestParam(required = false) Long selectPartM2,
                                                                      @RequestParam(required = false) Long selectPartS2, @RequestParam(required = false) String partnerSearch2,
                                                                      @RequestParam(required = false) Long selectProL2, @RequestParam(required = false) Long selectProM2,
                                                                      @RequestParam(required = false) Long selectProS2, @RequestParam(required = false) String productSearch2,
                                                                      @RequestParam(required = false) Long selectMaterL2, @RequestParam(required = false) Long selectMaterM2,
                                                                      @RequestParam(required = false) Long selectMaterS2, @RequestParam(required = false) String materialSearch2,
                                                                      @RequestParam(required = false) Long baljuProgress2){
        if (partnerSearch2 != null && partnerSearch2.trim().isEmpty()) { partnerSearch2 = null; }
        if (productSearch2 != null && productSearch2.trim().isEmpty()) { productSearch2 = null; }
        if (materialSearch2 != null && materialSearch2.trim().isEmpty()) { materialSearch2 = null; }

        PageRequestDTO2 requestDTO=PageRequestDTO2.builder()
                .page2(page2).size2(10)
                .partL2(selectPartL2).partM2(selectPartM2).partS2(selectPartS2).partnerSearch2(partnerSearch2)
                .proL2(selectProL2).proM2(selectProM2).proS2(selectProS2).productSearch2(productSearch2)
                .materL2(selectMaterL2).materM2(selectMaterM2).materS2(selectMaterS2).materialSearch2(materialSearch2)
                .baljuProgress2(baljuProgress2).build();

        return baljuService.finBaljuPage(requestDTO);
    }

    @GetMapping("/nonGumsuBalju")
    public PageResultDTO<BaljuGumsuDTO, Object[]> nonGumsuBalju(@RequestParam(required = false) int page,
                                                                @RequestParam(required = false) Long selectPartL, @RequestParam(required = false) Long selectPartM,
                                                                @RequestParam(required = false) Long selectPartS, @RequestParam(required = false) String partnerSearch,
                                                                @RequestParam(required = false) Long selectProL, @RequestParam(required = false) Long selectProM,
                                                                @RequestParam(required = false) Long selectProS, @RequestParam(required = false) String productSearch,
                                                                @RequestParam(required = false) Long selectMaterL, @RequestParam(required = false) Long selectMaterM,
                                                                @RequestParam(required = false) Long selectMaterS, @RequestParam(required = false) String materialSearch){

        if (partnerSearch != null && partnerSearch.trim().isEmpty()) { partnerSearch = null; }
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }
        if (materialSearch != null && materialSearch.trim().isEmpty()) { materialSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .partL(selectPartL).partM(selectPartM).partS(selectPartS).partnerSearch(partnerSearch)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .materL(selectMaterL).materM(selectMaterM).materS(selectMaterS).materialSearch(materialSearch).build();

        return gumsuService.couldGumsu(requestDTO);
    }

    // 검수차수> 진행도
    @GetMapping("/gumsuChasuList")
    public PageResultDTO<GumsuChasuContractDTO, Object[]> gumsuChasuList(@RequestParam(required = false) int page,
                                                                         @RequestParam(required = false) Long selectPartL, @RequestParam(required = false) Long selectPartM,
                                                                         @RequestParam(required = false) Long selectPartS, @RequestParam(required = false) String partnerSearch,
                                                                         @RequestParam(required = false) Long selectProL, @RequestParam(required = false) Long selectProM,
                                                                         @RequestParam(required = false) Long selectProS, @RequestParam(required = false) String productSearch,
                                                                         @RequestParam(required = false) Long selectMaterL, @RequestParam(required = false) Long selectMaterM,
                                                                         @RequestParam(required = false) Long selectMaterS, @RequestParam(required = false) String materialSearch){

        if (partnerSearch != null && partnerSearch.trim().isEmpty()) { partnerSearch = null; }
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }
        if (materialSearch != null && materialSearch.trim().isEmpty()) { materialSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .partL(selectPartL).partM(selectPartM).partS(selectPartS).partnerSearch(partnerSearch)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .materL(selectMaterL).materM(selectMaterM).materS(selectMaterS).materialSearch(materialSearch).build();

        return gumsuChasuService.getAllGumsuChasuContract(requestDTO);
    }

    @GetMapping("/shipmentList")
    public PageResultDTO<ShipmentGumsuDTO, Object[]> shipmentList(@RequestParam(required = false) int page,
                                                                  @RequestParam(required = false) Long selectPartL, @RequestParam(required = false) Long selectPartM,
                                                                  @RequestParam(required = false) Long selectPartS, @RequestParam(required = false) String partnerSearch,
                                                                  @RequestParam(required = false) Long selectProL, @RequestParam(required = false) Long selectProM,
                                                                  @RequestParam(required = false) Long selectProS, @RequestParam(required = false) String productSearch,
                                                                  @RequestParam(required = false) Long selectMaterL, @RequestParam(required = false) Long selectMaterM,
                                                                  @RequestParam(required = false) Long selectMaterS, @RequestParam(required = false) String materialSearch,
                                                                  @RequestParam(required = false) Long baljuFin, @RequestParam(required = false) Long receiveReturn){

        if (partnerSearch != null && partnerSearch.trim().isEmpty()) { partnerSearch = null; }
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }
        if (materialSearch != null && materialSearch.trim().isEmpty()) { materialSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .partL(selectPartL).partM(selectPartM).partS(selectPartS).partnerSearch(partnerSearch)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .materL(selectMaterL).materM(selectMaterM).materS(selectMaterS).materialSearch(materialSearch)
                .baljuFin(baljuFin).receiveReturn(receiveReturn).build();

        return shipmentService.pageShipment(requestDTO);
    }

    @GetMapping("/nonReciveShipment")
    public PageResultDTO<ShipmentGumsuDTO, Object[]> nonReciveShipment(@RequestParam(required = false) int page, @RequestParam(required = false) Long baljuProductName){
        PageRequestDTO requestDTO=PageRequestDTO.builder().page(page).size(10).baljuProductName(baljuProductName).build();
        return shipmentService.mainShipment(requestDTO);
    }

    @GetMapping("/nonInvoiceShipment")
    public PageResultDTO<ShipmentDTO, Object[]> nonInvoiceShipment(@RequestParam(required = false) int page,
                                                                   @RequestParam(required = false) Long selectPartL, @RequestParam(required = false) Long selectPartM,
                                                                   @RequestParam(required = false) Long selectPartS, @RequestParam(required = false) String partnerSearch,
                                                                   @RequestParam(required = false) Long selectProL, @RequestParam(required = false) Long selectProM,
                                                                   @RequestParam(required = false) Long selectProS, @RequestParam(required = false) String productSearch,
                                                                   @RequestParam(required = false) Long selectMaterL, @RequestParam(required = false) Long selectMaterM,
                                                                   @RequestParam(required = false) Long selectMaterS, @RequestParam(required = false) String materialSearch){

        if (partnerSearch != null && partnerSearch.trim().isEmpty()) { partnerSearch = null; }
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }
        if (materialSearch != null && materialSearch.trim().isEmpty()) { materialSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .partL(selectPartL).partM(selectPartM).partS(selectPartS).partnerSearch(partnerSearch)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .materL(selectMaterL).materM(selectMaterM).materS(selectMaterS).materialSearch(materialSearch).build();

        return shipmentService.noneInvoiceShipment(requestDTO);
    }

    @GetMapping("/yesInvoiceShipment")
    public PageResultDTO<InvoicePartnerDTO, Object[]> yesInvoiceShipment(@RequestParam(required = false) int page2,
                                                                        @RequestParam(required = false) Long selectPartL2, @RequestParam(required = false) Long selectPartM2,
                                                                        @RequestParam(required = false) Long selectPartS2, @RequestParam(required = false) String partnerSearch2,
                                                                        @RequestParam(required = false) Long selectProL2, @RequestParam(required = false) Long selectProM2,
                                                                        @RequestParam(required = false) Long selectProS2, @RequestParam(required = false) String productSearch2,
                                                                        @RequestParam(required = false) Long selectMaterL2, @RequestParam(required = false) Long selectMaterM2,
                                                                        @RequestParam(required = false) Long selectMaterS2, @RequestParam(required = false) String materialSearch2){

        if (partnerSearch2 != null && partnerSearch2.trim().isEmpty()) { partnerSearch2 = null; }
        if (productSearch2 != null && productSearch2.trim().isEmpty()) { productSearch2 = null; }
        if (materialSearch2 != null && materialSearch2.trim().isEmpty()) { materialSearch2 = null; }

        PageRequestDTO2 requestDTO=PageRequestDTO2.builder()
                .page2(page2).size2(10)
                .partL2(selectPartL2).partM2(selectPartM2).partS2(selectPartS2).partnerSearch2(partnerSearch2)
                .proL2(selectProL2).proM2(selectProM2).proS2(selectProS2).productSearch2(productSearch2)
                .materL2(selectMaterL2).materM2(selectMaterM2).materS2(selectMaterS2).materialSearch2(materialSearch2).build();

        return shipmentService.pageFinInvoice(requestDTO);
    }

    // 협력회사> 계약서 목록
    @GetMapping("/partnerContractList")
    public PageResultDTO<ContractDTO, Contract> partnerContractList(@RequestParam(required = false) int page){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthPartnerDTO authPartnerDTO=(AuthPartnerDTO) authentication.getPrincipal();

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .pno(authPartnerDTO.getPno()).build();
        return contractService.partnerContractList(requestDTO);
    }
    // 협력회사> 발주서 목록
    @GetMapping("/partnerOrderList")
    public PageResultDTO<BaljuDTO, Balju> partnerOrderList(@RequestParam(required = false) int page){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthPartnerDTO authPartnerDTO=(AuthPartnerDTO) authentication.getPrincipal();

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .pno(authPartnerDTO.getPno()).build();
        return baljuService.partnerBaljuList(requestDTO);
    }
    // 협력회사> 거래명세서 목록
    @GetMapping("/partnerInvoiceList")
    public PageResultDTO<InvoiceContractDTO, Object[]> partnerInvoiceList(@RequestParam(required = false) int page){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthPartnerDTO authPartnerDTO=(AuthPartnerDTO) authentication.getPrincipal();

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .pno(authPartnerDTO.getPno()).build();
        return shipmentService.partnerInvoicePage(requestDTO);
    }
    // 협력회사> 반품 목록
    @GetMapping("/partnerReturnList")
    public PageResultDTO<ReturnsDTO, Returns> partnerReturnList(@RequestParam(required = false) int page, Long selectedBox){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthPartnerDTO authPartnerDTO=(AuthPartnerDTO) authentication.getPrincipal();

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .pno(authPartnerDTO.getPno()).returnCheck(selectedBox).build();

        return returnsService.getReturnPage(requestDTO);
    }
    // 협력회사> 긴급납품 목록
    @GetMapping("/partnerUrgencyList")
    public PageResultDTO<EmergencyDTO, Emergency> partnerUrgencyList(@RequestParam(required = false) int page, Long selectedBox){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        AuthPartnerDTO authPartnerDTO=(AuthPartnerDTO) authentication.getPrincipal();

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .pno(authPartnerDTO.getPno()).urgencyCheck(selectedBox).build();

        return emergencyService.getAllEmergencies(requestDTO);
    }

    // 재고> 수량 목록
    @PostMapping("/getStockList")
    public PageResultDTO<StockQuantityDTO, Object[]> getStockList(
            @RequestParam(required = false) int page, @RequestParam(required = false) Long stockStatus,
            @RequestParam(required = false) Long orderStatus, @RequestParam(required = false) Long selectedBox,
            @RequestParam(required = false) Long selectProL, @RequestParam(required = false) Long selectProM,
            @RequestParam(required = false) Long selectProS, @RequestParam(required = false) String productSearch,
            @RequestParam(required = false) Long selectMaterL, @RequestParam(required = false) Long selectMaterM,
            @RequestParam(required = false) Long selectMaterS, @RequestParam(required = false) String materialSearch){
        if (productSearch != null && productSearch.trim().isEmpty()) { productSearch = null; }
        if (materialSearch != null && materialSearch.trim().isEmpty()) { materialSearch = null; }
        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .materL(selectMaterL).materM(selectMaterM).materS(selectMaterS).materialSearch(materialSearch)
                .box(selectedBox).baljuStatus(orderStatus).stockStatus(stockStatus)
                .build();
        return contractService.stockList(requestDTO);
    }
    // 출하요청> 출하요청 목록
    @GetMapping("/getRequestList")
    public PageResultDTO<PreRequestCountDTO, Object[]> getRequestList(
            @RequestParam(required = false) int page, @RequestParam(required = false) Long releaseStatus,
            @RequestParam(required = false) Long selectProL, @RequestParam(required = false) Long selectProM,
            @RequestParam(required = false) Long selectProS, @RequestParam(required = false) String productSearch){
        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .proL(selectProL).proM(selectProM).proS(selectProS).productSearch(productSearch)
                .releaseStatus(releaseStatus)
                .build();
        return preRequestService.requestPage(requestDTO);
    }



    // 카테고리 관리> 협력회사 카테고리 목록
    @GetMapping("/getPagePart")
    public PageResultDTO<CategoryPartDTO, Object[]> getPagePart(
            @RequestParam(required = false) int page, @RequestParam(required = false) Long code){
        Pageable pageable= PageRequest.of(page-1, 12);
        return partCodeService.getPagePart(pageable, code);
    }
    // 카테고리 관리> 제품 카테고리 목록
    @GetMapping("/getPagePro")
    public PageResultDTO<CategoryProDTO, Object[]> getPagePro(
            @RequestParam(required = false) int page, @RequestParam(required = false) Long code){
        Pageable pageable= PageRequest.of(page-1, 12);
        return proCodeService.getPagePro(pageable, code);
    }
    // 카테고리 관리> 자재 카테고리 목록
    @GetMapping("/getPageMater")
    public PageResultDTO<CategoryMaterDTO, Object[]> getPageMater(
            @RequestParam(required = false) int page, @RequestParam(required = false) Long code){
        Pageable pageable= PageRequest.of(page-1, 12);
        return materService.getPageMater(pageable, code);
    }

    // 관리자> 직원목록
    @GetMapping("/getMemberPage")
    public PageResultDTO<MemberDTO, Member> getMemberPage(
            @RequestParam(required = false) int page, @RequestParam(required = false) String department,
            @RequestParam(required = false) String role, @RequestParam(required = false) String memberSearch){
        if (department != null && department.trim().isEmpty()) { department = null; }
        if (role != null && role.trim().isEmpty()) { role = null; }
        if (memberSearch != null && memberSearch.trim().isEmpty()) { memberSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .department(department).role(role).memberSearch(memberSearch)
                .build();
        return memberService.findMemberList(requestDTO);
    }
    // 관리자> 회사목록
    @GetMapping("/getPartnerPage")
    public PageResultDTO<PartnerDTO, Partner> getPartnerPage(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) Long selectPartL, @RequestParam(required = false) Long selectPartM,
            @RequestParam(required = false) Long selectPartS, @RequestParam(required = false) String partnerSearch){
        if (partnerSearch != null && partnerSearch.trim().isEmpty()) { partnerSearch = null; }

        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .page(page).size(15)
                .partL(selectPartL).partM(selectPartM).partS(selectPartS).partnerSearch(partnerSearch)
                .build();
        return partnerService.findPartnerList(requestDTO);
    }
    // 메인>달력
    @GetMapping("/getCalendar")
    public List<CalendarDTO> getCalendar(){
        List<CalendarDTO> calendarDTOList=requestService.getAllRequest();
        List<CalendarDTO> baljuChasuList=baljuChasuService.getAllBaljuChasu();
        calendarDTOList.addAll(baljuChasuList);
        return calendarDTOList;
    }
}
