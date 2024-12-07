package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.PreRequestRepository;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.PreRequestCountDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.PreRequestSturctureDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanSturctureDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PreRequestServiceImpl implements PreRequestService{
    private final PreRequestRepository preRequestRepository;
    private final LineService lineService;
    private final MemberService memberService;
    private final ProplanService proplanService;

    // 저장, 삭제
    @Override
    public PreRequestDTO savePreRequest(PreRequestDTO preRequestDTO){
        PreRequest saved=preRequestRepository.save(dtoToEntity(preRequestDTO));
        return entityToDTO(saved);
    }
    @Override
    public void deletePreRequest(Long preCode){
        preRequestRepository.deleteById(preCode);
    }
    @Override
    public void updateAllCheck(Long preReqCode){
        preRequestRepository.updateAllCheck(preReqCode);
    }

    // 변환
    @Override
    public PreRequest dtoToEntity(PreRequestDTO dto){
        return PreRequest.builder()
                .preReqCode(dto.getPreReqCode())
                .allCheck(dto.getAllCheck())
                .text(dto.getText())
                .line(lineService.dtoToEntity(dto.getLineDTO()))
                .proPlan(proplanService.dtoToEntity(dto.getProplanDTO()))
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO()))
                .build();
    }
    @Override
    public PreRequestDTO entityToDTO(PreRequest entity){
        return PreRequestDTO.builder()
                .preReqCode(entity.getPreReqCode())
                .allCheck(entity.getAllCheck())
                .text(entity.getText())
                .regDate(entity.getRegDate())
                .lineDTO(lineService.entityToDto(entity.getLine()))
                .proplanDTO(proplanService.entityToDTO(entity.getProPlan()))
                .memberDTO(memberService.memberTodto(entity.getWriter()))
                .build();
    }

    // 조회
    @Override
    public PreRequestDTO getPreRequest(Long preCode){
        PreRequest preRequest=preRequestRepository.findById(preCode).get();
        return entityToDTO(preRequest);
    }


    // 출하요청> 출하목록
    @Override
    public PageResultDTO<PreRequestCountDTO, Object[]> requestPage(PageRequestDTO requestDTO){
        Page<Object[]> entityPage=preRequestRepository.requestPage(requestDTO);
        return new PageResultDTO<>(entityPage, this::PreRequestCountDTOToDTO);
    }
    private PreRequestCountDTO PreRequestCountDTOToDTO(Object[] objects) {
        PreRequest preRequest = (PreRequest) objects[0];
        Long tempAllCount = (Long) objects[1];
        Long tempFinCount = (Long) objects[2];

        PreRequestDTO preRequestDTO = (preRequest != null) ? entityToDTO(preRequest) : null;
        Long allCount = (tempAllCount != null) ? tempAllCount : 0L;
        Long finCount = (tempFinCount != null) ? tempFinCount : 0L;

        return new PreRequestCountDTO(preRequestDTO, allCount, finCount);
    }
}