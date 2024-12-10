package mit.iwrcore.IWRCore.security.dto.multiDTO;

import lombok.Builder;
import lombok.Data;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterSDTO;

@Builder
@Data
public class CategoryMaterDTO {
    private MaterLDTO materLDTO;
    private MaterMDTO materMDTO;
    private MaterSDTO materSDTO;

    public CategoryMaterDTO(MaterLDTO materLDTO, MaterMDTO materMDTO, MaterSDTO materSDTO){
        this.materLDTO=materLDTO;
        this.materMDTO=materMDTO;
        this.materSDTO=materSDTO;
    }
}
