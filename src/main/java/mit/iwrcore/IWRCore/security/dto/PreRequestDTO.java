package mit.iwrcore.IWRCore.security.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PreRequestDTO {
    private Long preReqCode;
    private Long allCheck;
    private String text;
    private LocalDateTime regDate;
    private LineDTO lineDTO;
    private ProplanDTO proplanDTO;
    private MemberDTO memberDTO;
}
