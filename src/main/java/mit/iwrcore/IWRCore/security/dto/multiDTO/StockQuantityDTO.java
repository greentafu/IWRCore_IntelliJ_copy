package mit.iwrcore.IWRCore.security.dto.multiDTO;

import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;

@Builder
@Data
public class StockQuantityDTO {
    private MaterialDTO materialDTO;
    private ContractDTO contractDTO;
    private Long shipNum;
    private Long reqNum;
    private Long orderNum;

    public StockQuantityDTO(MaterialDTO materialDTO, ContractDTO contractDTO, Long shipNum, Long reqNum, Long orderNum){
        this.materialDTO=materialDTO;
        this.contractDTO=contractDTO;
        this.shipNum=shipNum;
        this.reqNum=reqNum;
        this.orderNum=orderNum;
    }
}
