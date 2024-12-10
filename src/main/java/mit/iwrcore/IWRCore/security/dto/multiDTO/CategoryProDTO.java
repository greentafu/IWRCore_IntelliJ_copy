package mit.iwrcore.IWRCore.security.dto.multiDTO;

import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProSDTO;

@Builder
@Data
public class CategoryProDTO {
    private ProLDTO proLDTO;
    private ProMDTO proMDTO;
    private ProSDTO proSDTO;

    public CategoryProDTO(ProLDTO proLDTO, ProMDTO proMDTO, ProSDTO proSDTO){
        this.proLDTO=proLDTO;
        this.proMDTO=proMDTO;
        this.proSDTO=proSDTO;
    }
}
