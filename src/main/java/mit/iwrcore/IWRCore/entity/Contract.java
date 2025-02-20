package mit.iwrcore.IWRCore.entity;

import com.querydsl.core.annotations.QueryInit;
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
@ToString(exclude = {"writer", "partner"})
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conNo;       // 계약서 번호
    @NotNull
    private Long conNum;      // 수량
    @NotNull
    private Long money;       // 단가
    @NotNull
    private LocalDateTime conDate;  // 체결일

    private String who;      // 담당자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")  // 외래 키 컬럼 이름
    @NotNull
    private Member writer;            // 작성자

    @OneToOne  // JodalPlan과의 1대1 관계
    @JoinColumn(name = "jodal_plan_id") // JodalPlan과의 외래 키
    private JodalPlan jodalPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")  // 외래 키 컬럼 이름
    @NotNull
    private Partner partner;           // 연관된 Partner 엔티티
    @OneToMany(mappedBy = "contract")  // Order 엔티티와의 1대다 관계
    private List<Balju> baljus;        // 이 Contract와 연관된 Order 목록

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileContract> files;
}

