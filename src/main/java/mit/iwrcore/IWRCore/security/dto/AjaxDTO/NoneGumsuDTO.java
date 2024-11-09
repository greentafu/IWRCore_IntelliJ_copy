package mit.iwrcore.IWRCore.security.dto.AjaxDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.security.dto.BaljuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.BaljuDTO;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class NoneGumsuDTO {
    private BaljuDTO baljuDTO;
    private List<BaljuChasuDTO> baljuChasuDTOs;
}
