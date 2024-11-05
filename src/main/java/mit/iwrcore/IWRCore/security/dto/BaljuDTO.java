package mit.iwrcore.IWRCore.security.dto;

import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.entity.BaljuChasu;

import java.time.LocalDateTime;
import java.util.List;

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
