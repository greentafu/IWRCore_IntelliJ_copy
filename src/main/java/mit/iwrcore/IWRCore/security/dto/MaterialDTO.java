package mit.iwrcore.IWRCore.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.FileMaterial;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterSDTO;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Getter
@Setter
@ToString
@Builder
public class MaterialDTO {
    private Long materCode;     //코드
    private String name;        //이름
    private String unit;        //단위
    private String standard;    //규격
    private String color;       //색상
    private LocalDateTime date;        //등록일자

    private BoxDTO boxDTO;
    private MaterSDTO materSDTO;
    private MemberDTO memberDTO;

    private List<FileMaterial> fileList;
}
