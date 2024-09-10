package mit.iwrcore.IWRCore.security.dto.PageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
    private int page;
    private int size;

    public PageRequestDTO(){
        this.page=1;
        this.size=15;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1, size, sort);
    }

    // 협력회사 번호
    private Long pno;

    // 관리자_회원검색
    private String department;
    private String role;
    private String memberSearch;
    // 협력회사검색
    private Long partL;
    private Long partM;
    private Long partS;
    private String partnerSearch;
    // 제품검색
    private Long proL;
    private Long proM;
    private Long proS;
    private String productSearch;
    private Long productImsiCheck;
    // 자재검색
    private Long materL;
    private Long materM;
    private Long materS;
    private String materialSearch;
    private Long box;
}
