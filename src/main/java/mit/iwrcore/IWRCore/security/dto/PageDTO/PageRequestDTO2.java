package mit.iwrcore.IWRCore.security.dto.PageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO2 {
    private int page2;
    private int size2;

    public PageRequestDTO2(){
        this.page2=1;
        this.size2=15;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page2-1, size2, sort);
    }

    // 협력회사 번호
    private Long pno2;

    // 관리자_회원검색
    private Integer department2;
    private Integer role2;
    private String memberSearch2;
    // 협력회사검색
    private Long partL2;
    private Long partM2;
    private Long partS2;
    private String partnerSearch2;
    // 제품검색
    private Long proL2;
    private Long proM2;
    private Long proS2;
    private String productSearch2;
    private Long productImsiCheck2;
    // 자재검색
    private Long materL2;
    private Long materM2;
    private Long materS2;
    private String materialSearch2;
    private Long box2;
    // 생산계획 진행
    private Long proplanProgress2;
    // 계약서 진행
    private Long progressContract2;
    // 발주 진행
    private Long baljuProgress2;

    // 계약서등록 중 조달계획들
    private List<Long> jodalPlans;
    // 배송지
    private String loc;

    // 거래명세서 등록 중 배송목록들
    private List<Long> shipments;
    // 거래명세서 수정 중 수정중인 거래명세서 번호
    private Long code;
}
