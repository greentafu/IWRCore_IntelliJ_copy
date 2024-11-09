package mit.iwrcore.IWRCore.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.*;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final InvoiceService invoiceService;
    private final BaljuService baljuService;
    private final MemberService memberService;
    private final ReturnsRepository returnsRepository;
    private final GumsuService gumsuService;
    private final PartnerService partnerService;

    // 저장, 삭제
    @Override
    @Transactional
    public ShipmentDTO saveShipment(ShipmentDTO shipmentDTO) {
        Shipment shipment = dtoToEntity(shipmentDTO);
        Shipment savedShipment = shipmentRepository.save(shipment);
        return entityToDTO(savedShipment);
    }
    @Override
    @Transactional
    public void deleteShipment(Long id) {
        shipmentRepository.deleteById(id);
    }
    @Override
    @Transactional
    public ShipmentDTO modifyShipment(Long id, ShipmentDTO shipmentDTO) {
        Shipment existingShipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID가 " + id + "인 ShipmentDTO를 찾을 수 없습니다."));

        if (shipmentDTO.getShipNum() != null) existingShipment.setShipNum(shipmentDTO.getShipNum());
        if (shipmentDTO.getReceipt() != null) existingShipment.setReceipt(shipmentDTO.getReceipt());

        if (shipmentDTO.getInvoiceDTO() != null) existingShipment.setInvoice(invoiceService.convertToEntity(shipmentDTO.getInvoiceDTO()));
        else existingShipment.setInvoice(null);

        if (shipmentDTO.getBaljuDTO() != null) existingShipment.setBalju(baljuService.dtoToEntity(shipmentDTO.getBaljuDTO()));
        else existingShipment.setBalju(null);

        if (shipmentDTO.getMemberDTO() != null) existingShipment.setWriter(memberService.memberdtoToEntity(shipmentDTO.getMemberDTO()));

        if (shipmentDTO.getReturnsDTO() != null) {
            Returns returns = returnsRepository.findById(shipmentDTO.getReturnsDTO().getReNO())
                    .orElseThrow(() -> new RuntimeException("Returns not found"));
            existingShipment.setReturns(returns);
        } else existingShipment.setReturns(null);

        Shipment updatedShipment = shipmentRepository.save(existingShipment);
        return entityToDTO(updatedShipment);
    }
    @Override
    @Transactional
    public void updateShipmentInvoicebGo(Long shipNo){
        shipmentRepository.updateShipmentInvoiceText(shipNo);
    }
    @Override
    public void updateShipmentDate(LocalDateTime dateTime, Long shipNo){
        shipmentRepository.updateShipmentDate(dateTime, shipNo);
    }
    @Override
    public void updateMemberCheck(Member member, Long shipNo){
        shipmentRepository.updateShipmentMemberCheck(member, shipNo);
    }
    @Override
    public void updateSHipmentInvoice(Invoice invoice, String text, Long shipNo){
        shipmentRepository.updateShipmentInvoice(invoice, text, shipNo);
    }

    // 변환
    @Override
    public Shipment dtoToEntity(ShipmentDTO dto) {
        return Shipment.builder()
                .shipNO(dto.getShipNO())
                .shipNum(dto.getShipNum())
                .receipt(dto.getReceipt())
                .text(dto.getText())
                .receiveCheck(dto.getReceiveCheck())
                .bGo(dto.getBGo())
                .writer(dto.getMemberDTO()!=null ? memberService.memberdtoToEntity(dto.getMemberDTO()) : null)
                .invoice(dto.getInvoiceDTO() != null ? invoiceService.convertToEntity(dto.getInvoiceDTO()) : null)
                .balju(dto.getBaljuDTO() != null ? baljuService.dtoToEntity(dto.getBaljuDTO()) : null)
                .build();
    }
    @Transactional
    @Override
    public ShipmentDTO entityToDTO(Shipment entity) {
        return ShipmentDTO.builder()
                .shipNO(entity.getShipNO())
                .shipNum(entity.getShipNum())
                .receipt(entity.getReceipt())
                .text(entity.getText())
                .receiveCheck(entity.getReceiveCheck())
                .regDate(entity.getRegDate())
                .bGo(entity.getBGo())
                .invoiceDTO(entity.getInvoice() != null ? invoiceService.convertToDTO(entity.getInvoice()) : null)
                .baljuDTO(entity.getBalju() != null ? baljuService.entityToDTO(entity.getBalju()) : null)
                .memberDTO(entity.getWriter()!=null ? memberService.memberTodto(entity.getWriter()) : null)
                .build();
    }

    // 조회
    @Override
    public ShipmentDTO getShipment(Long id) {
        Shipment shipment=shipmentRepository.findById(id).get();
        return entityToDTO(shipment);
    }
    @Override
    public Shipment getShipmentEntity(Long shipNo){
        return shipmentRepository.findShipment(shipNo);
    }
    @Override
    public List<ShipmentDTO> getShipmentByBalju(Long baljuNo){
        List<Shipment> entityList=shipmentRepository.getShipmentByBalju(baljuNo);
        return entityList.stream().map(this::entityToDTO).toList();
    }










    @Override
    public List<ShipmentDTO> getInvoiceContent(Long tranNO){
        return shipmentRepository.getInvoiceContent(tranNO).stream().map(this::entityToDTO).toList();
    }
    @Override
    public ShipmentReturnDTO findShipment(Long shipNo){
        Shipment shipment=shipmentRepository.findShipment(shipNo);
        if(shipment!=null){
            String materialName=shipment.getBalju().getContract().getJodalPlan().getMaterial().getName();
            return new ShipmentReturnDTO(shipNo, shipment.getShipNum(), materialName);
        }else return null;
    }
    @Override
    public List<ShipmentDTO> canInvoiceShipment(Long pno){
        List<Shipment> entityList=shipmentRepository.couldInvoice(pno);
        return entityList.stream().map(x->entityToDTO(x)).toList();
    }
    private ShipmentDTO extractShipmentDTO(Object[] objects){
        Shipment shipment=(Shipment) objects[0];
        ShipmentDTO shipmentDTO=(shipment!=null)?entityToDTO(shipment):null;
        return shipmentDTO;
    }
    @Override
    public List<PartnerDTO> canInvoicePartner(){
        List<Partner> entityList=shipmentRepository.couldInvoicePartner();
        List<PartnerDTO> dtoList=new ArrayList<>();
        entityList.stream().forEach(x->dtoList.add(partnerService.partnerTodto(x)));
        return dtoList;
    }









    // 메인페이지> 배송갯수
    @Override
    public Long mainShipNum(){
        Long aa= shipmentRepository.mainShipment();
        return (aa!=null)?aa:0L;
    }
    // 메인페이지> 수령가능 목록
    @Override
    public PageResultDTO<ShipmentGumsuDTO, Object[]> mainShipment(PageRequestDTO requestDTO){
        Page<Object[]> entityPage=shipmentRepository.findShipmentByCustomQuery2(requestDTO);
        return new PageResultDTO<>(entityPage, this::shipmentGumsuToDTO);
    }
    private ShipmentGumsuDTO shipmentGumsuToDTO(Object[] objects){
        Shipment shipment=(Shipment) objects[0];
        Gumsu gumsu=(Gumsu) objects[1];
        Long totalShipment=(Long) objects[2];
        Long reNo=(Long) objects[3];
        ShipmentDTO shipmentDTO=(shipment!=null)? entityToDTO(shipment):null;
        GumsuDTO gumsuDTO=(gumsu!=null)? gumsuService.entityToDTO(gumsu):null;
        totalShipment=(totalShipment!=null)?totalShipment:0L;
        reNo=(reNo!=null)?reNo:0L;
        return new ShipmentGumsuDTO(shipmentDTO, gumsuDTO, totalShipment, reNo);
    }
    // 입고/반품> 배송 목록
    @Override
    public PageResultDTO<ShipmentGumsuDTO, Object[]> pageShipment(PageRequestDTO requestDTO){
        Page<Object[]> entityPage=shipmentRepository.findShipmentByCustomQuery(requestDTO);
        return new PageResultDTO<>(entityPage, this::shipmentGumsuToDTO);
    }
    // 거래명세서> 거래명세서 발급 필요 목록
    @Override
    public PageResultDTO<ShipmentDTO, Object[]> noneInvoiceShipment(PageRequestDTO requestDTO){
        Page<Object[]> entityPage=shipmentRepository.findShipmentByCustomQuery3(requestDTO);
        return new PageResultDTO<>(entityPage, this::shipmentContractToDTO);
    }
    private ShipmentDTO shipmentContractToDTO(Object[] objects){
        Shipment shipment=(Shipment) objects[0];
        return (shipment!=null)? entityToDTO(shipment):null;
    }
    // 거래명세서> 거래명세서 발급 완료 목록
    @Override
    public PageResultDTO<InvoicePartnerDTO, Object[]> pageFinInvoice(PageRequestDTO2 requestDTO2){
        Page<Object[]> entityPage=shipmentRepository.findShipmentByCustomQuery4(requestDTO2);
        return new PageResultDTO<>(entityPage, this::invoicePartnerToDTO);
    }
    private InvoicePartnerDTO invoicePartnerToDTO(Object[] objects){
        Invoice invoice=(Invoice) objects[0];
        Partner partner=(Partner) objects[1];
        InvoiceDTO invoiceDTO=(invoice!=null)? invoiceService.convertToDTO(invoice):null;
        PartnerDTO partnerDTO=(partner!=null)? partnerService.partnerTodto(partner):null;
        return new InvoicePartnerDTO(invoiceDTO, partnerDTO);
    }
    // 협력회사> 거래명세서 목록
    @Override
    public PageResultDTO<InvoicePartnerDTO, Object[]> partnerInvoicePage(PageRequestDTO requestDTO){
        Pageable pageable=requestDTO.getPageable(Sort.by("tranNO").descending());
        Page<Object[]> entityPage=shipmentRepository.partnerInvoicePage(pageable, requestDTO.getPno());
        return new PageResultDTO<>(entityPage, this::invoicePartnerToDTO);
    }




    @Override
    public List<ShipmentDTO> getShipmentsByReceiveCheck(long receiveCheck) {
        List<Shipment> shipments = shipmentRepository.findByReceiveCheckWithDetails(receiveCheck);

        List<ShipmentDTO> shipmentDTOs = shipments.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());

        log.info("Converted Shipment Entities to DTOs: {}", shipmentDTOs);
        return shipmentDTOs;
    }

}
