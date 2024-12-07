package mit.iwrcore.IWRCore.security.dto.multiDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LLLLLLSSDTO {
    private Long long1;
    private Long long2;
    private Long long3;
    private Long long4;
    private Long long5;
    private Long long6;
    private String string1;
    private String string2;

    public LLLLLLSSDTO(@JsonProperty("long1") Long long1,
                       @JsonProperty("long2") Long long2,
                       @JsonProperty("long3") Long long3,
                       @JsonProperty("long4") Long long4,
                       @JsonProperty("long5") Long long5,
                       @JsonProperty("long6") Long long6,
                       @JsonProperty("string1") String string1,
                       @JsonProperty("string2") String string2){
        this.long1=long1;
        this.long2=long2;
        this.long3=long3;
        this.long4=long4;
        this.long5=long5;
        this.long6=long6;
        this.string1=string1;
        this.string2=string2;
    }
}
