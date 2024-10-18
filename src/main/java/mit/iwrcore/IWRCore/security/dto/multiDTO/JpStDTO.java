package mit.iwrcore.IWRCore.security.dto.multiDTO;

import lombok.*;
import mit.iwrcore.IWRCore.security.dto.JodalPlanDTO;
import mit.iwrcore.IWRCore.security.dto.StructureDTO;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class JpStDTO {
    private JodalPlanDTO jodalPlanDTO;
    private StructureDTO structureDTO;
}
