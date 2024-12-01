package mit.iwrcore.IWRCore.security.dto;

import lombok.*;
import mit.iwrcore.IWRCore.entity.FileContract;
import mit.iwrcore.IWRCore.entity.FileMaterial;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileContractDTO;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ContractDTO {
    private Long conNo;
    private Long conNum;
    private Long money;
    private LocalDateTime conDate;
    private String who;
    private LocalDateTime regDate;

    private JodalPlanDTO jodalPlanDTO;
    private MemberDTO memberDTO;
    private PartnerDTO partnerDTO;

    private List<FileContractDTO> fileList;
}