package mit.iwrcore.IWRCore.security.dto.multiDTO;

import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;

@Builder
@Data
public class StockDetailDTO {
    private Integer date;
    private ContractDTO contractDTO;
    private Long sumShipNum;
    private Long sumRequestNum;

    public StockDetailDTO(Integer date, ContractDTO contractDTO, Long sumShipNum, Long sumRequestNum){
        this.date=date;
        this.contractDTO=contractDTO;
        this.sumShipNum=sumShipNum;
        this.sumRequestNum=sumRequestNum;
    }
}
