package mit.iwrcore.IWRCore.security.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InvoiceDTO {
    private Long tranNO;                // 거래명세서 번호
    private String plz;                  // 영수증 여부
    private LocalDateTime dateCreated; // 작성일
    private String email1;
    private String email2;

    private String text;
    private Long cash;
    private Long cheque;
    private Long promissory;
    private Long receivable;

    private MemberDTO memberDTO;             // 작성자 ID
}
