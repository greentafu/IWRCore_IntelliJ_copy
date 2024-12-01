package mit.iwrcore.IWRCore.security.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProplanDTO {
    private Long proplanNo;
    private Long pronum;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime regDate;
    private String details;
    private Long finCheck;
    private ProductDTO productDTO;
    private MemberDTO memberDTO;
}
