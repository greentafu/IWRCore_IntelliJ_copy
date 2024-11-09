package mit.iwrcore.IWRCore.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.GumsuChasuRepository;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.GumsuChasuContractDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.QuantityDateDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GumsuChasuServiceImpl implements GumsuChasuService{
    private final GumsuChasuRepository gumsuChasuRepository;
    private final MemberService memberService;
    private final GumsuService gumsuService;
    private final ContractService contractService;
    private final JodalChasuService jodalChasuService;

    // 저장, 삭제
    @Override
    public void saveGumsuChasu(GumsuChasuDTO gumsuChasuDTO) {
        GumsuChasu gumsuChasu = dtoToEntity(gumsuChasuDTO);
        gumsuChasuRepository.save(gumsuChasu);
    }
    @Override
    public void deleteGumsuChasu(Long id) {
        gumsuChasuRepository.deleteById(id);
    }
    @Override
    public GumsuChasuDTO modifyGumsuChasu(GumsuChasuDTO gumsuChasuDTO){
        GumsuChasu gumsuChasu=gumsuChasuRepository.findById(gumsuChasuDTO.getGcnum()).orElseThrow(() -> new EntityNotFoundException("GumsuChasu not found"));
        gumsuChasu.setGumsuNum(gumsuChasuDTO.getGumsuNum());
        gumsuChasu.setGumsuDate(gumsuChasuDTO.getGumsuDate());
        GumsuChasu savedGumsuChasu=gumsuChasuRepository.save(gumsuChasu);
        return entityToDTO(savedGumsuChasu);
    }

    // 변환
    @Override
    public GumsuChasu dtoToEntity(GumsuChasuDTO dto) {
        return GumsuChasu.builder()
                .gcnum(dto.getGcnum())
                .gumsuNum(dto.getGumsuNum())
                .gumsuDate(dto.getGumsuDate())
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO()))
                .gumsu(gumsuService.dtoToEntity(dto.getGumsuDTO()))
                .build();
    }
    @Override
    public GumsuChasuDTO entityToDTO(GumsuChasu entity) {
        return GumsuChasuDTO.builder()
                .gcnum(entity.getGcnum())
                .gumsuNum(entity.getGumsuNum())
                .gumsuDate(entity.getGumsuDate())
                .memberDTO(memberService.memberTodto(entity.getWriter()))
                .gumsuDTO(gumsuService.entityToDTO(entity.getGumsu()))
                .build();
    }

    // 조회
    @Override
    public GumsuChasuDTO getGumsuChasu(Long id) {
        return entityToDTO(gumsuChasuRepository.findById(id).get());
    }


    // 검수차수> 진행도
    @Override
    public PageResultDTO<GumsuChasuContractDTO, Object[]> getAllGumsuChasuContract(PageRequestDTO requestDTO){
        Page<Object[]> entityPage=gumsuChasuRepository.findGumsuChasuByCustomQuery(requestDTO);
        return new PageResultDTO<>(entityPage, this::gumsuChasuContractToDTO);
    }
    private GumsuChasuContractDTO gumsuChasuContractToDTO(Object[] objects){
        GumsuChasu gumsuChasu=(GumsuChasu) objects[0];
        Contract contract=(Contract) objects[1];
        Long allShipNum=(Long) objects[2];
        GumsuChasuDTO gumsuChasuDTO=(gumsuChasu!=null)?entityToDTO(gumsuChasu):null;
        ContractDTO contractDTO=(contract!=null)?contractService.entityToDTO(contract):null;
        Long allNum=(allShipNum!=null)?allShipNum:0L;

        Long remainingDate=0L;
        Double percent=0d;

        Long tempSum=0L;

        if(gumsuChasuDTO!=null){
            LocalDateTime gumsuDate=gumsuChasuDTO.getGumsuDate();
            LocalDateTime today=LocalDateTime.now();
            remainingDate= Duration.between(today, gumsuDate).toDays();
        }

        List<JodalChasuDTO> jodalChasuDTOs=jodalChasuService.getJodalChasuByJodalPlan(contractDTO.getJodalPlanDTO().getJoNo())
                .stream()
                .sorted(Comparator.comparing(JodalChasuDTO::getJcnum))
                .toList();
        List<GumsuChasu> gumsuChasuList=gumsuChasuRepository.getGumsuChasuByBaljuNo(gumsuChasuDTO.getGumsuDTO().getBaljuDTO().getBaljuNo())
                .stream()
                .sorted(Comparator.comparing(GumsuChasu::getGcnum))
                .toList();

        for(int i=0; i<3; i++){
            if(gumsuChasuList.get(i).getGcnum()<gumsuChasuDTO.getGcnum()){
                tempSum+=jodalChasuDTOs.get(i).getJoNum();
            }
        }

        percent=(double)(gumsuChasuDTO.getGumsuDTO().getMake()-tempSum)*100d/gumsuChasuDTO.getGumsuNum();
        if(percent>=100) percent=100d;
        else if(percent<=0) percent=0d;
        else percent=percent;

        return new GumsuChasuContractDTO(gumsuChasuDTO, contractDTO, allNum, remainingDate, percent);
    }
    // 검수차수> 검수차수 저장에 사용
    @Override
    public List<GumsuChasuDTO> getGumsuChasuByGumsu(Long gumsuNo){
        List<GumsuChasu> entityList=gumsuChasuRepository.getGumsuChasuListByGumsu(gumsuNo);
        return entityList.stream().map(this::entityToDTO).toList();
    }
    // 협력회사> 메인페이지 검수정보
    @Override
    public List<QuantityDateDTO> partnerMainGumsu(Long baljuNo){
        List<GumsuChasu> entityList=gumsuChasuRepository.getGumsuChasuByBaljuNo(baljuNo);
        List<QuantityDateDTO> list=new ArrayList<>();
        for(GumsuChasu gumsuChasu:entityList){
            QuantityDateDTO quantityDateDTO=QuantityDateDTO.builder()
                    .quantity(gumsuChasu.getGumsuNum())
                    .dueDate(gumsuChasu.getGumsuDate())
                    .build();
            list.add(quantityDateDTO);
        }
        if(list.size()>0) list.get(0).setTotalOrder(entityList.get(0).getGumsu().getMake());
        return list;
    }
}