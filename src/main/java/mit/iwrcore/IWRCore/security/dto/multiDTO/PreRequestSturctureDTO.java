package mit.iwrcore.IWRCore.security.dto.multiDTO;

import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.security.dto.*;

@Builder
@Data
public class PreRequestSturctureDTO {
    private PreRequestDTO preRequestDTO;
    private StructureDTO structureDTO;
    private RequestDTO requestDTO;
    private Long sumRequest;
    private Long sumShip;

    public PreRequestSturctureDTO(PreRequestDTO preRequestDTO, StructureDTO structureDTO, RequestDTO requestDTO,
                                  Long sumRequest, Long sumShip){
        this.preRequestDTO=preRequestDTO;
        this.structureDTO=structureDTO;
        this.requestDTO=requestDTO;
        this.sumRequest=sumRequest;
        this.sumShip=sumShip;
    }
}
