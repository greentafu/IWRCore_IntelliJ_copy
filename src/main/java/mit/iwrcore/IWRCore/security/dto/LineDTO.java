package mit.iwrcore.IWRCore.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
@ToString
@Builder
@AllArgsConstructor
public class LineDTO {
    private Long lineCode;
    private String lineName;
}
