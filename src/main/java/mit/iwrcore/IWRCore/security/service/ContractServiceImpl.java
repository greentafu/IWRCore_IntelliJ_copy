package mit.iwrcore.IWRCore.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.ContractRepository;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ContractJodalChasuDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.NewOrderDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.StockDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.StockDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final MemberService memberService;
    private final JodalPlanService jodalPlanService;
    private final PartnerService partnerService;
    private final JodalChasuService jodalChasuService;
    private final MaterialService materialService;

    // 저장, 삭제
    @Override
    public ContractDTO saveContract(ContractDTO contractDTO, List<FileContract> fileList) {
        Contract contract = dtoToEntity(contractDTO);
        contract.setFiles(fileList);
        Contract savedContract = contractRepository.save(contract);
        return entityToDTO(savedContract);
    }
    @Override
    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }

    //변환
    @Override
    public Contract dtoToEntity(ContractDTO dto) {
        return Contract.builder()
                .conNo(dto.getConNo())
                .conNum(dto.getConNum())
                .money(dto.getMoney())
                .conDate(dto.getConDate())
                .who(dto.getWho())
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO()))
                .jodalPlan(jodalPlanService.dtoToEntity(dto.getJodalPlanDTO()))
                .partner(partnerService.partnerDtoToEntity(dto.getPartnerDTO()))
                .build();
    }
    @Override
    public ContractDTO entityToDTO(Contract entity) {
        return ContractDTO.builder()
                .conNo(entity.getConNo())
                .conNum(entity.getConNum())
                .money(entity.getMoney())
                .conDate(entity.getConDate())
                .who(entity.getWho())
                .regDate(entity.getRegDate())
                .jodalPlanDTO(jodalPlanService.entityToDTO(entity.getJodalPlan()))
                .memberDTO(memberService.memberTodto(entity.getWriter()))
                .partnerDTO(partnerService.partnerTodto(entity.getPartner()))
                .build();
    }

    // 조회
    @Override
    public ContractDTO getContract(Long id) {
        return entityToDTO(contractRepository.getReferenceById(id));
    }
    @Override
    public ContractDTO getContractByJodalPlan(Long joNo){
        Contract contract=contractRepository.getContractByJodalPlan(joNo);
        if(contract==null) return null;
        return entityToDTO(contract);
    }


    // 조달계획> 조달차수 있는(조달계획한) 자재 목록+계약서 등록여부
    @Override
    public PageResultDTO<ContractJodalChasuDTO, Object[]> yesJodalplanMaterial(PageRequestDTO2 requestDTO) {
        Page<Object[]> entityPage=contractRepository.findContractByCustomQuery(requestDTO);
        return new PageResultDTO<>(entityPage, this::JodalPlanContractToDTO);
    }
    // 계약서> 계약서 등록해야 하는 조달계획목록
    @Override
    public PageResultDTO<ContractJodalChasuDTO, Object[]> couldContractMaterial(PageRequestDTO requestDTO) {
        Page<Object[]> entityPage=contractRepository.findContractByCustomQuery2(requestDTO);
        return new PageResultDTO<>(entityPage, this::JodalPlanContractToDTO);
    }
    private ContractJodalChasuDTO JodalPlanContractToDTO(Object[] objects){
        JodalPlan jodalPlan=(JodalPlan) objects[0];
        Contract contract=(Contract) objects[1];
        JodalChasu jodalChasu=(JodalChasu) objects[2];
        Long allNum=(Long) objects[3];

        ContractDTO contractDTO=(contract!=null)?entityToDTO(contract):null;
        JodalPlanDTO jodalPlanDTO=(jodalPlan!=null)?jodalPlanService.entityToDTO(jodalPlan):null;
        JodalChasuDTO jodalChasuDTO=(jodalChasu!=null)?jodalChasuService.entityToDTO(jodalChasu):null;
        Long num=(allNum!=null)?allNum:0L;

        return new ContractJodalChasuDTO(jodalPlanDTO, contractDTO, jodalChasuDTO, num);
    }
    // 발주서> 발주해야 하는 계약목록(협력회사로 묶음)
    @Override
    public List<NewOrderDTO> newOrderContract(Long pno){
        List<Object[]> entityList=contractRepository.newOrderContract(pno);
        List<NewOrderDTO> newOrderDTOList=new ArrayList<>();
        Set<ContractDTO> contractDTOSet=new HashSet<>();
        Set<JodalChasuDTO> jodalChasuDTOSet=new HashSet<>();

        for(Object[] objects:entityList){
            Contract contract=(Contract) objects[0];
            JodalChasu jodalChasu=(JodalChasu) objects[1];
            ContractDTO contractDTO=(contract!=null)?entityToDTO(contract):null;
            JodalChasuDTO jodalChasuDTO=(jodalChasu!=null)?jodalChasuService.entityToDTO(jodalChasu):null;
            contractDTOSet.add(contractDTO);
            jodalChasuDTOSet.add(jodalChasuDTO);

            if(jodalChasuDTOSet.size()==3){
                ContractDTO saveContractDTO=contractDTOSet.stream().toList().get(0);
                List<JodalChasuDTO> saveJodalChasuDTOList=jodalChasuDTOSet.stream().toList();
                List<JodalChasuDTO> sortedJodalChsasuList=saveJodalChasuDTOList.stream()
                        .sorted(Comparator.comparing(JodalChasuDTO::getJcnum))
                        .collect(Collectors.toList());

                NewOrderDTO newOrderDTO=new NewOrderDTO(saveContractDTO, sortedJodalChsasuList, null);
                newOrderDTOList.add(newOrderDTO);
                contractDTOSet.clear();
                jodalChasuDTOSet.clear();
            }
        }
        return newOrderDTOList;
    }
    // 협력회사> 협력회사용 계약서목록
    @Override
    public PageResultDTO<ContractDTO, Contract> partnerContractList(PageRequestDTO requestDTO) {
        Page<Contract> entityPage=contractRepository.partnerContractPage(requestDTO);
        Function<Contract, ContractDTO> fn = (entity -> entityToDTO(entity));
        return new PageResultDTO<>(entityPage, fn);
    }




    // 재고관리> 재고목록??
    @Override
    public List<StockDTO> stockList(){
        List<Object[]> entityList=contractRepository.stockList();
        List<StockDTO> dtoList=entityList.stream().map(this::exStockDTO).toList();
        return dtoList;
    }
    private StockDTO exStockDTO(Object[] objects){
        Material material=(Material) objects[0];
        Long tempMoney=(Long) objects[1];
        Long tempSumShip=(Long) objects[2];
        Long tempSumReq=(Long) objects[3];
        Long tempCountBal=(Long) objects[4];

        MaterialDTO materialDTO=(material!=null)?materialService.entityToDto(material):null;
        Long money=(tempMoney!=null)?tempMoney:0L;
        Long sumShip=(tempSumShip!=null)?tempSumShip:0L;
        Long sumReq=(tempSumReq!=null)?tempSumReq:0L;
        Long countBal=(tempCountBal!=null)?tempCountBal:0L;

        return new StockDTO(materialDTO, money, sumShip, sumReq, countBal);
    }
    @Override
    public List<StockDetailDTO> detailStock(Long materCode){
        List<Object[]> entityList=contractRepository.detailStock(materCode);
        List<StockDetailDTO> dtoList=entityList.stream().map(this::exStockDetailDTO).toList();
        return dtoList;
    }
    private StockDetailDTO exStockDetailDTO(Object[] objects){
        Contract contract=(Contract) objects[0];
        Long tempSumShip=(Long) objects[1];
        Long tempSumReq=(Long) objects[2];

        ContractDTO contractDTO=(contract!=null)?entityToDTO(contract):null;
        Long sumShip=(tempSumShip!=null)?tempSumShip:0L;
        Long sumReq=(tempSumReq!=null)?tempSumReq:0L;
        return new StockDetailDTO(contractDTO, sumShip, sumReq);
    }

}


