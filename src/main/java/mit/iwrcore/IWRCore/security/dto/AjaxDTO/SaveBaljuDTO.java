package mit.iwrcore.IWRCore.security.dto.AjaxDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class SaveBaljuDTO {
    private Long baljuNo;
    private Long conNo;
    private String baljuWhere;
    private String baljuPlz;
    private Long oneNum;
    private String oneDate;
    private Long twoNum;
    private String twoDate;
    private Long threeNum;
    private String threeDate;
}
