package mit.iwrcore.IWRCore.security.dto;

import lombok.*;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProSDTO;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ProductDTO {
    private Long manuCode;
    private String name;
    private String color;
    private String text;
    private String uuid;
    private String supervisor;
    private Long mater_imsi;
    private Long mater_check;
    private LocalDateTime regDate;
    //외래키설정
    private ProSDTO proSDTO;
    private MemberDTO memberDTO;
    private List<ProplanDTO> proPlans;
}
