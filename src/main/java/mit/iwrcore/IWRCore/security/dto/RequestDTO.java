package mit.iwrcore.IWRCore.security.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestDTO {
    private Long requestCode;
    private Long requestNum;
    private LocalDateTime eventDate;
    private LocalDateTime releaseDate;

    private MaterialDTO materialDTO;
    private PreRequestDTO preRequestDTO;
}
