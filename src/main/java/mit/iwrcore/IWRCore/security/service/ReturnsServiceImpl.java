package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;

import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.ReturnsRepository;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ReturnBaljuDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReturnsServiceImpl implements ReturnsService {
    private static final Logger log = LoggerFactory.getLogger(ReturnsServiceImpl.class);
    private final ReturnsRepository returnsRepository;
    private final ShipmentService shipmentService;
    private final MemberService memberService;
    private final BaljuService baljuService;

    // 저장, 삭제
    @Override
    public ReturnsDTO saveReturns(ReturnsDTO returnsDTO, List<FileReturns> fileList) {
        Returns returns=dtoToEntity(returnsDTO);
        returns.setFiles(fileList);
        Returns saved = returnsRepository.save(returns);
        return entityToDTO(saved);
    }
    @Override
    public void deleteReturns(Long id) {
        returnsRepository.deleteById(id);
    }

    @Override
    public void modifyReturnCheck(Long reNO){
        returnsRepository.updateReturnsCheck(reNO);
    }

    // 변환
    @Override
    public Returns dtoToEntity(ReturnsDTO dto) {
        return Returns.builder()
                .reNO(dto.getReNO())
                .reDetail(dto.getReDetail())
                .whyRe(dto.getWhyRe())
                .bGo(dto.getBGo())
                .email(dto.getEmail())
                .returnCheck(dto.getReturnCheck())
                .shipment(shipmentService.dtoToEntity(dto.getShipmentDTO()))
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO()))
                .build();
    }
    @Override
    public ReturnsDTO entityToDTO(Returns entity) {
        return ReturnsDTO.builder()
                .reNO(entity.getReNO())
                .reDetail(entity.getReDetail())
                .whyRe(entity.getWhyRe())
                .bGo(entity.getBGo())
                .email(entity.getEmail())
                .returnCheck(entity.getReturnCheck())
                .regDate(entity.getRegDate())
                .shipmentDTO(shipmentService.entityToDto(entity.getShipment()))
                .memberDTO(memberService.memberTodto(entity.getWriter()))
                .build();
    }

    // 조회
    @Override
    public ReturnsDTO getReturns(Long id) {
        Returns returns=returnsRepository.findById(id).get();
        return entityToDTO(returns);
    }


    // 협력회사> 반품 목록 확인
    @Override
    public List<ReturnsDTO> getReturnsList(Long baljuNo){
        List<Returns> entityList=returnsRepository.getReturns(baljuNo);
        return entityList.stream().map(this::entityToDTO).toList();
    }
    // 협력회사> 반품 목록
    @Override
    public PageResultDTO<ReturnBaljuDTO, Object[]> getReturnPage(PageRequestDTO requestDTO, Long pno) {
        Pageable pageable=requestDTO.getPageable(Sort.by("reNO").descending());
        Page<Object[]> entityPage=returnsRepository.pageReturns(pageable, pno);
        Function<Object[], ReturnBaljuDTO> fn=(entity->returnBaljuToDTO(entity));
        return new PageResultDTO<>(entityPage, fn);
    }
    private ReturnBaljuDTO returnBaljuToDTO(Object[] objects) {
        Returns returns=(Returns) objects[0];
        Long shipNum=(Long) objects[1];
        LocalDateTime regDate=(LocalDateTime) objects[2];
        Balju balju=(Balju) objects[3];
        ReturnsDTO returnsDTO=entityToDTO(returns);
        BaljuDTO baljuDTO=baljuService.entityToDTO(balju);
        return new ReturnBaljuDTO(returnsDTO, shipNum, regDate, baljuDTO);
    }
}