package mit.iwrcore.IWRCore.security.dto.multiDTO;

import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.security.dto.PreRequestDTO;

@Builder
@Data
public class PreRequestCountDTO {
    private PreRequestDTO preRequestDTO;
    private Long allCount;
    private Long finCount;

    public PreRequestCountDTO(PreRequestDTO preRequestDTO, Long allCount, Long finCount){
        this.preRequestDTO=preRequestDTO;
        this.allCount=allCount;
        this.finCount=finCount;
    }
}
