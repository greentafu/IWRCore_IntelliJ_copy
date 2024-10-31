package mit.iwrcore.IWRCore.security.dto.FileDTO;

import lombok.*;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FileContractDTO {
    private String uuid;
    private String uploadPath;
    private String fileName;
    private String contentType;
    private int imgType;
    private ContractDTO contractDTO;
}
