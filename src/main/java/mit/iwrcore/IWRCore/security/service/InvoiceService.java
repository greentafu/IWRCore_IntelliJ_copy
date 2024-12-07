package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Invoice;
import mit.iwrcore.IWRCore.security.dto.InvoiceDTO;

public interface InvoiceService {
    // 저장, 삭제
    InvoiceDTO saveInvoice(InvoiceDTO invoiceDTO);
    void deleteInvoice(Long id);
    InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO);

    // 변환
    Invoice dtoToEntity(InvoiceDTO dto);
    InvoiceDTO entityToDto(Invoice entity);

    // 조회
    InvoiceDTO getInvoiceById(Long id);
}