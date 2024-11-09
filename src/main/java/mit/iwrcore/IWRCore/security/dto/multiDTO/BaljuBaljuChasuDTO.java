package mit.iwrcore.IWRCore.security.dto.multiDTO;

import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.security.dto.BaljuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.BaljuDTO;

@Builder
@Data
public class BaljuBaljuChasuDTO {
    private BaljuDTO baljuDTO;
    private BaljuChasuDTO baljuChasuDTO;

    public BaljuBaljuChasuDTO(BaljuDTO baljuDTO, BaljuChasuDTO baljuChasuDTO){
        this.baljuDTO=baljuDTO;
        this.baljuChasuDTO=baljuChasuDTO;
    }
}
