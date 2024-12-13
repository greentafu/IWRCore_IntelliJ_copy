package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.*;
;
import mit.iwrcore.IWRCore.repository.EmergencyRepository;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.LLLSDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyServiceImpl implements EmergencyService{
    private final EmergencyRepository emergencyRepository;
    private final BaljuService baljuService;
    private final MemberService memberService;
    private final GumsuService gumsuService;
    private final ContractService contractService;

    // 저장, 삭제
    @Override
    public EmergencyDTO saveEmergency(EmergencyDTO emergencyDTO) {
        Emergency emergency = dtoToEntity(emergencyDTO);
        Emergency savedEmergency = emergencyRepository.save(emergency);
        return entityToDTO(savedEmergency);
    }
    @Override
    public void deleteEmergency(Long id) {
        emergencyRepository.deleteById(id);
    }
    @Override
    public void updateEmergencyCheck(Long emerNo){
        emergencyRepository.updateEmergencyCheck(emerNo);
    }

    // 변환
    @Override
    public Emergency dtoToEntity(EmergencyDTO dto) {
        return Emergency.builder()
                .emerNo(dto.getEmerNo())
                .emerNum(dto.getEmerNum())
                .emerDate(dto.getEmerDate())
                .who(dto.getWho())
                .emcheck(dto.getEmcheck())
                .balju(dto.getBaljuDTO() != null ? baljuService.dtoToEntity(dto.getBaljuDTO()) : null)
                .writer(dto.getMemberDTO() != null ? memberService.memberdtoToEntity(dto.getMemberDTO()) : null)
                .build();
    }
    @Override
    public EmergencyDTO entityToDTO(Emergency entity) {
        return EmergencyDTO.builder()
                .emerNo(entity.getEmerNo())
                .emerNum(entity.getEmerNum())
                .emerDate(entity.getEmerDate())
                .who(entity.getWho())
                .emcheck(entity.getEmcheck())
                .regDate(entity.getRegDate())
                .baljuDTO(baljuService.entityToDTO(entity.getBalju()))
                .memberDTO(memberService.memberTodto(entity.getWriter()))
                .build();
    }

    // 조회
    @Override
    public EmergencyDTO getEmergencyById(Long id) {
        Emergency emergency=emergencyRepository.findById(id).get();
        return entityToDTO(emergency);
    }


    // 협력회사> 깁근납품 목록
    @Override
    public PageResultDTO<EmergencyDTO, Emergency> getAllEmergencies(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("emerNo").descending());
        Page<Emergency> entityPage=null;
        if(requestDTO.getUrgencyCheck()==null) entityPage=emergencyRepository.findPartnerAllEmergency(pageable, requestDTO.getPno());
        else if(requestDTO.getUrgencyCheck()==0L) entityPage=emergencyRepository.findPartnerNonCheckEmergency(pageable, requestDTO.getPno());
        else if(requestDTO.getUrgencyCheck()==1L) entityPage=emergencyRepository.findPartnerCheckEmergency(pageable, requestDTO.getPno());
        Function<Emergency, EmergencyDTO> fn = (entity -> entityToDTO(entity));
        return new PageResultDTO<>(entityPage, fn);
    }
    // 협력회사> 메인화면 발주별 긴급납품 여부 확인용
    @Override
    public List<EmergencyDTO> getEmergencyByBalju(Long baljuNo) {
        List<Emergency> entityList = emergencyRepository.getEmengencyListByBalju(baljuNo);
        return entityList.stream().map(this::entityToDTO).collect(Collectors.toList());
    }
    // 긴급납품> 긴급납품 목록(생산계획, 자재코드)
    @Override
    public PageResultDTO<EmergencyDTO, Emergency> getEmergencyByProMat(Pageable pageable, Long proplanNo, Long materCode){
        Page<Emergency> entitiyPage=emergencyRepository.findEmergency(pageable, materCode, proplanNo);
        Function<Emergency, EmergencyDTO> fn = (entity -> entityToDTO(entity));
        PageResultDTO<EmergencyDTO, Emergency> result=new PageResultDTO<>(entitiyPage, fn);
        LLLSDTO lllsdto=gumsuService.getEveryQuantity(proplanNo, materCode);
        if(lllsdto!=null){
            result.setAllMakeNum(lllsdto.getLong1());
            result.setAllShipNum(lllsdto.getLong2());
            result.setAllReturnNum(lllsdto.getLong3());
        }
        ContractDTO contractDTO=contractService.getContractByProMater(proplanNo, materCode);
        result.setContractDTO(contractDTO);
        return result;
    }
}