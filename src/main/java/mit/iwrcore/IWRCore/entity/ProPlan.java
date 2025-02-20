package mit.iwrcore.IWRCore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"writer", "product"})
public class ProPlan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proplanNo;       // 생산계획 번호
    @NotNull
    private Long pronum;         // 수량
    @NotNull
    private LocalDateTime startDate;  // 시작일
    @NotNull
    private LocalDateTime endDate;    // 마감일

    private String details;      // 상세내용
    private Long finCheck;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")  // 외래 키 컬럼 이름
    @NotNull
    private Member writer;           // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @NotNull
    private Product product;      // Product 엔티티와의 관계

    @OneToMany(mappedBy = "proPlan")
    private List<JodalPlan> jodalPlans;

    @OneToMany(mappedBy = "proPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileProPlan> files;
}
