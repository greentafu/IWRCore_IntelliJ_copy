package mit.iwrcore.IWRCore.security.dto.FileDTO;

import lombok.*;
import mit.iwrcore.IWRCore.security.dto.ReturnsDTO;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FileReturnsDTO {
    private String uuid;
    private String uploadPath;
    private String fileName;
    private String contentType;
    private int imgType;
    private ReturnsDTO returnsDTO;
}
