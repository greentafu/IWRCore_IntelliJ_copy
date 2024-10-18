package mit.iwrcore.IWRCore.security.dto.FileDTO;

import lombok.*;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FileProductDTO {
    private String uuid;
    private String uploadPath;
    private String fileName;
    private String contentType;
    private int imgType;
    private ProductDTO productDTO;
}
