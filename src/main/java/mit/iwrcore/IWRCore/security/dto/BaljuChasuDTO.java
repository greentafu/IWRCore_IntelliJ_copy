package mit.iwrcore.IWRCore.security.dto;

import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.entity.Balju;

import java.time.LocalDateTime;

@Data
@Builder
public class BaljuChasuDTO {
    private Long balNo;
    private Long balNum;
    private LocalDateTime balDate;
    private BaljuDTO baljuDTO;
}
