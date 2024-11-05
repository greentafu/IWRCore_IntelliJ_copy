package mit.iwrcore.IWRCore.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ToString
public class LocationDTO {
    private Long lno;
    private String location;
    private LocalDateTime useTime;
}
