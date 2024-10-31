package mit.iwrcore.IWRCore.security.dto.multiDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LLLSDTO {
    private Long long1;
    private Long long2;
    private Long long3;
    private String string;

    public LLLSDTO(@JsonProperty("long1") Long long1,
                   @JsonProperty("long2") Long long2,
                   @JsonProperty("long3") Long long3,
                   @JsonProperty("string") String string){
        this.long1=long1;
        this.long2=long2;
        this.long3=long3;
        this.string=string;
    }
}
