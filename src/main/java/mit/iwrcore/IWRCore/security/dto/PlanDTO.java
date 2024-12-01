package mit.iwrcore.IWRCore.security.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.Line;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Data
@ToString
@Builder
@AllArgsConstructor
public class PlanDTO {
    private Long plancode;
    private ProductDTO productDTO;
    private Long quantity;
    private LineDTO line;
}
