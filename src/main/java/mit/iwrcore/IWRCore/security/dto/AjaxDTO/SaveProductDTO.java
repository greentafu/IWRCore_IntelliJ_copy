package mit.iwrcore.IWRCore.security.dto.AjaxDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class SaveProductDTO {
    private Long manuCode;
    private String person;
    private String productName;
    private Long selectProS;
    private String proColor;
    private String proText;
    private String materQuantityDTOs;
    private Long sel;
}
