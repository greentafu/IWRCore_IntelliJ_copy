package mit.iwrcore.IWRCore.security.dto.multiDTO;

import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartSDTO;

@Builder
@Data
public class CategoryPartDTO {
    private PartLDTO partLDTO;
    private PartMDTO partMDTO;
    private PartSDTO partSDTO;

    public CategoryPartDTO(PartLDTO partLDTO, PartMDTO partMDTO, PartSDTO partSDTO){
        this.partLDTO=partLDTO;
        this.partMDTO=partMDTO;
        this.partSDTO=partSDTO;
    }
}
