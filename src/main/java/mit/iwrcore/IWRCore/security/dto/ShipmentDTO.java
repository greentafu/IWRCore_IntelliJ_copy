package mit.iwrcore.IWRCore.security.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ShipmentDTO {
    private Long shipNO;
    private Long shipNum;
    private LocalDateTime receipt;
    private String text;
    private Long receiveCheck;
    private LocalDateTime regDate;
    private String bGo;

    private ReturnsDTO returnsDTO;
    private InvoiceDTO invoiceDTO;
    private BaljuDTO baljuDTO;
    private MemberDTO memberDTO;
}
