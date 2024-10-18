package mit.iwrcore.IWRCore.security.dto.AjaxDTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Data
public class MaterQuantityDTO {
    private Long code;
    private Long quantity;
    private Long sno;

    @JsonCreator
    public MaterQuantityDTO(@JsonProperty("code") Long code,
                            @JsonProperty("quantity") Long quantity,
                            @JsonProperty("sno") Long sno) {
        this.code = code;
        this.quantity = quantity;
        this.sno = sno;
    }
}
