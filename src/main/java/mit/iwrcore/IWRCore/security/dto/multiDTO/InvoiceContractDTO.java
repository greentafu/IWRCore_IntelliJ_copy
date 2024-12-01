package mit.iwrcore.IWRCore.security.dto.multiDTO;

import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.InvoiceDTO;

@Builder
@Data
public class InvoiceContractDTO {
    private InvoiceDTO invoiceDTO;
    private ContractDTO contractDTO;

    public InvoiceContractDTO(InvoiceDTO invoiceDTO, ContractDTO contractDTO){
        this.invoiceDTO=invoiceDTO;
        this.contractDTO=contractDTO;
    }
}
