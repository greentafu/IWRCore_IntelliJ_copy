package mit.iwrcore.IWRCore.security.dto.AjaxDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class SaveGumsuDTO {
    private Long gumsuNo;
    private Long baljuNo;
    private String person;
    private Long partnerNo;
    private Long oneNum;
    private String oneDate;
    private Long twoNum;
    private String twoDate;
    private Long threeNum;
    private String threeDate;
}
