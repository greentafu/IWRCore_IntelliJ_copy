package mit.iwrcore.IWRCore.security.dto.FileDTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AttachFileDTO {
    private String fileName;
    private String uploadPath;
    private String uuid;
    private String type;
    private boolean image;
}
