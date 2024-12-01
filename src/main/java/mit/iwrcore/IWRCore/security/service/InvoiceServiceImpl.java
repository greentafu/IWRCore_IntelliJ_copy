package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.Contract;
import mit.iwrcore.IWRCore.entity.Invoice;
import mit.iwrcore.IWRCore.repository.InvoiceRepository;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.InvoiceDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService{
    private final InvoiceRepository invoiceRepository;
    private final MemberService memberService;

    // 저장, 삭제
    @Override
    public InvoiceDTO saveInvoice(InvoiceDTO invoiceDTO) {
        Invoice invoice = dtoToEntity(invoiceDTO);
        Invoice saved = invoiceRepository.save(invoice);
        return entityToDto(saved);
    }
    @Override
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    // 변환
    @Override
    public Invoice dtoToEntity(InvoiceDTO dto) {
        return Invoice.builder()
                .tranNO(dto.getTranNO())
                .plz(dto.getPlz())
                .dateCreated(dto.getDateCreated())
                .email1(dto.getEmail1())
                .email2(dto.getEmail2())
                .text(dto.getText())
                .cash(dto.getCash())
                .cheque(dto.getCheque())
                .promissory(dto.getPromissory())
                .receivable(dto.getReceivable())
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO()))
                .build();
    }
    @Override
    public InvoiceDTO entityToDto(Invoice entity) {
        return InvoiceDTO.builder()
                .tranNO(entity.getTranNO())
                .plz(entity.getPlz())
                .dateCreated(entity.getDateCreated())
                .email1(entity.getEmail1())
                .email2(entity.getEmail2())
                .text(entity.getText())
                .cash(entity.getCash())
                .cheque(entity.getCheque())
                .promissory(entity.getPromissory())
                .receivable(entity.getReceivable())
                .memberDTO(memberService.memberTodto(entity.getWriter()))
                .build();
    }

    // 조회
    @Override
    public InvoiceDTO getInvoiceById(Long id) {
        return entityToDto(invoiceRepository.findById(id).get());
    }

    @Override
    public InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        if (!invoiceRepository.existsById(id)) {
            throw new RuntimeException("ID가 " + id + "인 InvoiceDTO를 찾을 수 없습니다.");
        }
        Invoice invoice = dtoToEntity(invoiceDTO);
        invoice.setTranNO(id); // 수정할 때 ID를 설정합니다.
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return entityToDto(updatedInvoice);
    }
}