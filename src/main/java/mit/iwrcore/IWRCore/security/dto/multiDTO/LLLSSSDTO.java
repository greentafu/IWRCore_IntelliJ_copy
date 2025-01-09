package mit.iwrcore.IWRCore.security.dto.multiDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LLLSSSDTO {
    private Long long1;
    private Long long2;
    private Long long3;
    private String string1;
    private String string2;
    private String string3;

    public LLLSSSDTO(@JsonProperty("long1") Long long1, @JsonProperty("long2") Long long2, @JsonProperty("long3") Long long3,
                   @JsonProperty("string") String string1, @JsonProperty("string") String string2, @JsonProperty("string") String string3){
        this.long1=long1;
        this.long2=long2;
        this.long3=long3;
        this.string1=string1;
        this.string2=string2;
        this.string3=string3;
    }
}
