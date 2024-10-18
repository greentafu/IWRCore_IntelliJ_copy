package mit.iwrcore.IWRCore.security.dto.FileDTO;

import lombok.*;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FileMaterialDTO {
    private String uuid;
    private String uploadPath;
    private String fileName;
    private String contentType;
    private int imgType;
    private MaterialDTO materialDTO;
}
