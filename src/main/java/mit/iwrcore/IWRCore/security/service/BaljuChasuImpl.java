package mit.iwrcore.IWRCore.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.BaljuChasuRepository;
import mit.iwrcore.IWRCore.security.dto.BaljuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.BaljuDTO;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.CalendarDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ModifyOrderDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.QuantityDateDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    @Override
    public BaljuChasuDTO modifyBalhuChasu(BaljuChasuDTO baljuChasuDTO){
        BaljuChasu baljuChasu=baljuChasuRepository.findById(baljuChasuDTO.getBalNo()).orElseThrow(() -> new EntityNotFoundException("BaljuChasu not found"));
        baljuChasu.setBalNum(baljuChasuDTO.getBalNum());
        baljuChasu.setBalDate(baljuChasuDTO.getBalDate());
        BaljuChasu savedBaljuChasu=baljuChasuRepository.save(baljuChasu);
        return entityToDto(savedBaljuChasu);
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
    public BaljuChasuDTO getBaljuChasu(Long balNo){
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
            ContractDTO contractDTO=(contract!=null)?contractService.entityToDTO(contract):null;
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
    @Override
    public ModifyOrderDTO getOneBalju(Long balNo){
        List<Object[]> entityList=baljuChasuRepository.getOneBalju(balNo);
        List<ModifyOrderDTO> modifyOrderDTOList=new ArrayList<>();
        Set<ContractDTO> contractDTOSet=new HashSet<>();
        Set<BaljuChasuDTO> baljuChasuDTOSet=new HashSet<>();
        Set<BaljuDTO> baljuDTOSet=new HashSet<>();

        for(Object[] objects:entityList){
            Contract contract=(Contract) objects[0];
            BaljuChasu baljuChasu=(BaljuChasu) objects[1];
            Balju balju=(Balju) objects[2];
            ContractDTO contractDTO=(contract!=null)?contractService.entityToDTO(contract):null;
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
        return modifyOrderDTOList.get(0);
    }
    @Override
    public List<CalendarDTO> getAllBaljuChasu(){
        List<CalendarDTO> result=new ArrayList<>();
        List<BaljuChasu> entityList=baljuChasuRepository.findAll();
        entityList.forEach(x->{
            String title=x.getBalju().getContract().getJodalPlan().getMaterial().getName()+"("+x.getBalNum()+"개)";
            String tempStartDate=x.getBalju().getRegDate().toString();
            String tempEndDate=x.getBalDate().toString();
            String startDate=(tempStartDate.contains("."))?tempStartDate.substring(0, 19):tempStartDate+":00";
            String endDate=(tempEndDate.contains("."))?tempStartDate.substring(0, 19):tempEndDate+":00";
            Long finCheck=x.getBalju().getFinCheck();
            String url="/order/order?baljuNo="+x.getBalju().getBaljuNo();
            result.add(new CalendarDTO(title, startDate, endDate, (finCheck==0)?2:3, url));
        });
        return result;
    }


    // 협력회사> 협력회사 메인화면 목록
    public List<QuantityDateDTO> partnerMainBalju(Long baljuNo, LocalDateTime baljuDate, Long shipNum){
        List<BaljuChasuDTO> baljuChasuDTOs=getBaljuChasuListByBaljuNo(baljuNo);
        List<QuantityDateDTO> list=new ArrayList<>();

        for(BaljuChasuDTO baljuChasuDTO:baljuChasuDTOs){
            QuantityDateDTO quantityDateDTO=QuantityDateDTO.builder()
                    .quantity(baljuChasuDTO.getBalNum())
                    .dueDate(baljuChasuDTO.getBalDate())
                    .build();
            list.add(quantityDateDTO);
        }
        LocalDateTime now=LocalDateTime.now();

        long entire1= ChronoUnit.DAYS.between(baljuDate, list.get(0).getDueDate()); // 전체 일수
        long current1= ChronoUnit.DAYS.between(baljuDate, now); // 현재까지의 일수
        Float temp1=Float.parseFloat(String.format("%.2f", current1*100f/entire1));
        if(current1==0L) temp1=0.0f;
        long nowBalju=ChronoUnit.DAYS.between(now, list.get(0).getDueDate());
        if(nowBalju<=0L) temp1=100.0f;

        if(temp1<0) list.get(0).setPercent(0f);
        else if(temp1>100) list.get(0).setPercent(100f);
        else list.get(0).setPercent(temp1);

        for(int i=0; i<list.size()-1; i++){
            long entire2= ChronoUnit.DAYS.between(list.get(i).getDueDate(), list.get(i+1).getDueDate()); // 전체 일수
            long current2=ChronoUnit.DAYS.between(list.get(i).getDueDate(), now); // 현재까지의 일수
            Float temp2=Float.parseFloat(String.format("%.2f", current2*100f/entire2));
            if(temp2<0) list.get(i+1).setPercent(0f);
            else if(temp2>100) list.get(i+1).setPercent(100f);
            else list.get(i+1).setPercent(temp2);
        }

        Long totalOrder=0L;
        Long sum=(shipNum!=null)?shipNum:0L;

        for(int i=0; i<list.size(); i++){
            long entire=list.get(i).getQuantity();
            totalOrder+=entire;
            if(sum>=entire){
                list.get(i).setCount(100.0f);
                sum-=entire;
            }else if(sum<entire){
                list.get(i).setCount(Float.parseFloat(String.format("%.2f", sum*100f/entire)));
                sum=0L;
            }
        }
        list.get(0).setTotalOrder(totalOrder);

        return list;
    }
}
