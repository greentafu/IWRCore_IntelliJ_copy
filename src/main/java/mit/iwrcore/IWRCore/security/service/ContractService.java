package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Contract;
import mit.iwrcore.IWRCore.entity.FileContract;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.*;

import java.util.List;

public interface ContractService {
    // 저장, 삭제
    ContractDTO saveContract(ContractDTO contractDTO, List<FileContract> fileList);
    void deleteContract(Long id);

    // 변환
    Contract dtoToEntity(ContractDTO dto);
    ContractDTO entityToDTO(Contract entity);

    // 조회
    ContractDTO getContract(Long id);
    List<Long> getConDateList();
    String getFirstConDateByMater(Long materCode);


    // 조달계획> 조달차수 있는(조달계획한) 자재 목록+계약서 등록여부
    PageResultDTO<ContractJodalChasuDTO, Object[]> yesJodalplanMaterial(PageRequestDTO2 requestDTO);
    // 계약서> 계약서 등록해야 하는 조달계획목록
    PageResultDTO<ContractJodalChasuDTO, Object[]> couldContractMaterial(PageRequestDTO requestDTO);
    // 발주서> 발주해야 하는 계약목록(협력회사로 묶음)
    List<NewOrderDTO> newOrderContract(Long pno);
    // 협력회사> 협력회사용 계약서목록
    PageResultDTO<ContractDTO, Contract> partnerContractList(PageRequestDTO requestDTO);
    // 재고> 재고목록
    PageResultDTO<StockQuantityDTO, Object[]> stockList(PageRequestDTO requestDTO);
    // 재고> 재고상세목록
    List<StockDetailDTO> stockdetailList(Long materCode, List<Integer> dateList);
    // 긴급요청> 계약(생산계획, 자재코드)
    ContractDTO getContractByProMater(Long proplanNo, Long materCode);
}