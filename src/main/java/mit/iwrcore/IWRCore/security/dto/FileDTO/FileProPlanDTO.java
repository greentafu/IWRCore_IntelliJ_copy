package mit.iwrcore.IWRCore.security.dto.FileDTO;

import lombok.*;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.security.dto.ProplanDTO;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FileProPlanDTO {
    private String uuid;
    private String uploadPath;
    private String fileName;
    private String contentType;
    private int imgType;
    private ProplanDTO proplanDTO;
}
