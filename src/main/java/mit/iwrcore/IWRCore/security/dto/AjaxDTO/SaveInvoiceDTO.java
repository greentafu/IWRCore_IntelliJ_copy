package mit.iwrcore.IWRCore.security.dto.AjaxDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class SaveInvoiceDTO {
    private Long tranNO;
    private String writeDate;
    private String text;
    private String email1;
    private String email2;
    private Long cash;
    private Long cheque;
    private Long promissory;
    private Long receivable;
    private String radio;
    private List<InvoiceTextDTO> invoiceTextDTOs;
}
