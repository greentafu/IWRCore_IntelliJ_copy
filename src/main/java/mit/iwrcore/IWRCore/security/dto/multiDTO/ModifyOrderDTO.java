package mit.iwrcore.IWRCore.security.dto.multiDTO;

import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.security.dto.BaljuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.BaljuDTO;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.JodalChasuDTO;

import java.util.List;

@Builder
@Data
public class ModifyOrderDTO {
    private ContractDTO contractDTO;
    private List<BaljuChasuDTO> baljuChasuDTOs;
    private BaljuDTO baljuDTO;
    private Long remainder;

    public ModifyOrderDTO(ContractDTO contractDTO, List<BaljuChasuDTO> baljuChasuDTOs, BaljuDTO baljuDTO, Long remainder){
        this.contractDTO=contractDTO;
        this.baljuChasuDTOs=baljuChasuDTOs;
        this.baljuDTO=baljuDTO;
        this.remainder=remainder;
    }
}
