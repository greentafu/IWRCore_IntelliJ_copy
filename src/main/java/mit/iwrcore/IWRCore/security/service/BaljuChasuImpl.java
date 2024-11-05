package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.Balju;
import mit.iwrcore.IWRCore.entity.BaljuChasu;
import mit.iwrcore.IWRCore.entity.Contract;
import mit.iwrcore.IWRCore.repository.BaljuChasuRepository;
import mit.iwrcore.IWRCore.security.dto.BaljuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.BaljuDTO;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ModifyOrderDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaljuChasuImpl implements BaljuChasuService{
    private final BaljuService baljuService;
    private final BaljuChasuRepository baljuChasuRepository;
    private final ContractService contractService;

    // 추가, 삭제
    @Override
    public BaljuChasuDTO saveBaljuChasu(BaljuChasuDTO baljuChasuDTO) {
        BaljuChasu baljuChasu=dtoToEntity(baljuChasuDTO);
        BaljuChasu savedBaljuChasu=baljuChasuRepository.save(baljuChasu);
        return entityToDto(savedBaljuChasu);
    }
    @Override
    public void deleteBaljuChasu(Long balNo){
        baljuChasuRepository.deleteById(balNo);
    }

    // 변환
    @Override
    public BaljuChasuDTO entityToDto(BaljuChasu entity){
        BaljuChasuDTO dto=BaljuChasuDTO.builder()
                .balNo(entity.getBalNo())
                .balNum(entity.getBalNum())
                .balDate(entity.getBalDate())
                .baljuDTO(baljuService.entityToDTO(entity.getBalju())).build();
        return dto;
    }
    @Override
    public BaljuChasu dtoToEntity(BaljuChasuDTO dto){
        BaljuChasu entity=BaljuChasu.builder()
                .balNo(dto.getBalNo())
                .balNum(dto.getBalNum())
                .balDate(dto.getBalDate())
                .balju(baljuService.dtoToEntity(dto.getBaljuDTO())).build();
        return entity;
    }

    // 조회
    @Override
    public BaljuChasuDTO getBaljuChasuById(Long balNo){
        BaljuChasuDTO baljuChasuDTO=null;
        if(balNo!=null) baljuChasuDTO=entityToDto(baljuChasuRepository.getById(balNo));
        return baljuChasuDTO;
    }
    @Override
    public List<BaljuChasuDTO> getBaljuChasuListByBaljuNo(Long baljuNo){
        List<BaljuChasuDTO> dtoList=new ArrayList<>();
        if(baljuNo!=null) {
            List<BaljuChasu> entityList=baljuChasuRepository.getBaljuChasuListByBaljuNo(baljuNo);
            if(entityList!=null) entityList.forEach(x->dtoList.add(entityToDto(x)));
        }
        return dtoList;
    }
    @Override
    public List<ModifyOrderDTO> modifyBalju(Long pno){
        List<Object[]> entityList=baljuChasuRepository.modifyBalju(pno);
        List<ModifyOrderDTO> modifyOrderDTOList=new ArrayList<>();
        Set<ContractDTO> contractDTOSet=new HashSet<>();
        Set<BaljuChasuDTO> baljuChasuDTOSet=new HashSet<>();
        Set<BaljuDTO> baljuDTOSet=new HashSet<>();

        for(Object[] objects:entityList){
            Contract contract=(Contract) objects[0];
            BaljuChasu baljuChasu=(BaljuChasu) objects[1];
            Balju balju=(Balju) objects[2];
            ContractDTO contractDTO=(contract!=null)?contractService.convertToDTO(contract):null;
            BaljuChasuDTO baljuChasuDTO=(baljuChasu!=null)?entityToDto(baljuChasu):null;
            BaljuDTO baljuDTO=(balju!=null)?baljuService.entityToDTO(balju):null;
            contractDTOSet.add(contractDTO);
            baljuChasuDTOSet.add(baljuChasuDTO);
            baljuDTOSet.add(baljuDTO);

            if(baljuChasuDTOSet.size()==3){
                ContractDTO saveContractDTO=contractDTOSet.stream().toList().get(0);
                List<BaljuChasuDTO> saveBaljuChasuDTOList=baljuChasuDTOSet.stream().toList();
                List<BaljuChasuDTO> sortedBaljuChsasuList=saveBaljuChasuDTOList.stream()
                        .sorted(Comparator.comparing(BaljuChasuDTO::getBalNo))
                        .collect(Collectors.toList());
                BaljuDTO saveBaljuDTO=baljuDTOSet.stream().toList().get(0);
                Long remainder=saveBaljuDTO.getBaljuNum()-saveContractDTO.getConNum();

                ModifyOrderDTO modifyOrderDTO=new ModifyOrderDTO(saveContractDTO, sortedBaljuChsasuList, saveBaljuDTO, remainder);
                modifyOrderDTOList.add(modifyOrderDTO);
                contractDTOSet.clear();
                baljuChasuDTOSet.clear();
                baljuDTOSet.clear();
            }
        }
        return modifyOrderDTOList;
    }
}
