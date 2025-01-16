package mit.iwrcore.IWRCore.security.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BaljuDTO {
    private Long baljuNo;
    private Long baljuNum;
    private LocalDateTime baljuDate;
    private String baljuWhere;
    private String baljuPlz;
    private Long finCheck;
    private LocalDateTime regDate;

    private MemberDTO memberDTO;
    private ContractDTO contractDTO;
}
