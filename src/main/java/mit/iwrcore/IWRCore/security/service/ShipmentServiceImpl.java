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
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final InvoiceService invoiceService;
    private final BaljuService baljuService;
    private final MemberService memberService;
    private final ContractService contractService;
    private final GumsuService gumsuService;
    private final PartnerService partnerService;

    // 저장, 삭제
    @Override
    @Transactional
    public ShipmentDTO saveShipment(ShipmentDTO shipmentDTO) {
        Shipment shipment = dtoToEntity(shipmentDTO);
        Shipment savedShipment = shipmentRepository.save(shipment);
        return entityToDto(savedShipment);
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
    public void updateShipmentInvoice(Invoice invoice, String text, Long shipNo){
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
                .invoice(dto.getInvoiceDTO() != null ? invoiceService.dtoToEntity(dto.getInvoiceDTO()) : null)
                .balju(dto.getBaljuDTO() != null ? baljuService.dtoToEntity(dto.getBaljuDTO()) : null)
                .build();
    }
    @Transactional
    @Override
    public ShipmentDTO entityToDto(Shipment entity) {
        return ShipmentDTO.builder()
                .shipNO(entity.getShipNO())
                .shipNum(entity.getShipNum())
                .receipt(entity.getReceipt())
                .text(entity.getText())
                .receiveCheck(entity.getReceiveCheck())
                .regDate(entity.getRegDate())
                .bGo(entity.getBGo())
                .invoiceDTO(entity.getInvoice() != null ? invoiceService.entityToDto(entity.getInvoice()) : null)
                .baljuDTO(entity.getBalju() != null ? baljuService.entityToDTO(entity.getBalju()) : null)
                .memberDTO(entity.getWriter()!=null ? memberService.memberTodto(entity.getWriter()) : null)
                .build();
    }

    // 조회
    @Override
    public ShipmentDTO getShipment(Long id) {
        Shipment shipment=shipmentRepository.findById(id).get();
        return entityToDto(shipment);
    }
    @Override
    public Shipment getShipmentEntity(Long shipNo){
        return shipmentRepository.findShipment(shipNo);
    }
    @Override
    public List<ShipmentDTO> getShipmentByBalju(Long baljuNo){
        List<Shipment> entityList=shipmentRepository.getShipmentByBalju(baljuNo);
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<ShipmentDTO> getShipmentByInvoice(Long tranNO){
        List<Shipment> entityList=shipmentRepository.getInvoiceContent(tranNO);
        return entityList.stream().map(this::entityToDto).toList();
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
    public Long getSavedShipNum(Long baljuNo){
        Long allShipNum=shipmentRepository.savedShipNumByBalju(baljuNo);
        return (allShipNum!=null)? allShipNum: 0L;
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
        ShipmentDTO shipmentDTO=(shipment!=null)? entityToDto(shipment):null;
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
        return (shipment!=null)? entityToDto(shipment):null;
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
        InvoiceDTO invoiceDTO=(invoice!=null)? invoiceService.entityToDto(invoice):null;
        PartnerDTO partnerDTO=(partner!=null)? partnerService.partnerTodto(partner):null;
        return new InvoicePartnerDTO(invoiceDTO, partnerDTO);
    }
    // 거래명세서> 거래명세서 발급 가능 배송 목록
    public PageResultDTO<ShipmentDTO, Shipment> couldInvoiceShipment(PageRequestDTO2 requestDTO2){
        Page<Shipment> entityPage=shipmentRepository.getInvoiceShipment(requestDTO2);
        Function<Shipment, ShipmentDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(entityPage, fn);
    }
    // 협력회사> 거래명세서 목록
    @Override
    public PageResultDTO<InvoiceContractDTO, Object[]> partnerInvoicePage(PageRequestDTO requestDTO){
        Page<Object[]> entityPage=shipmentRepository.partnerInvoicePage(requestDTO);
        return new PageResultDTO<>(entityPage, this::invoiceContractToDTO);
    }
    private InvoiceContractDTO invoiceContractToDTO(Object[] objects){
        Invoice invoice=(Invoice) objects[0];
        Contract contract=(Contract) objects[1];
        InvoiceDTO invoiceDTO=(invoice!=null)? invoiceService.entityToDto(invoice):null;
        ContractDTO contractDTO=(contract!=null)? contractService.entityToDTO(contract):null;
        return new InvoiceContractDTO(invoiceDTO, contractDTO);
    }




    @Override
    public List<ShipmentDTO> getShipmentsByReceiveCheck(long receiveCheck) {
        List<Shipment> shipments = shipmentRepository.findByReceiveCheckWithDetails(receiveCheck);

        List<ShipmentDTO> shipmentDTOs = shipments.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        log.info("Converted Shipment Entities to DTOs: {}", shipmentDTOs);
        return shipmentDTOs;
    }

}
