package mit.iwrcore.IWRCore.security.dto.multiDTO;

import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.security.dto.BaljuDTO;

@Builder
@Data
public class BaljuLLDTO {
    private BaljuDTO baljuDTO;
    private Long long1;
    private Long long2;

    public BaljuLLDTO(BaljuDTO baljuDTO, Long long1, Long long2){
        this.baljuDTO=baljuDTO;
        this.long1=long1;
        this.long2=long2;
    }
}
