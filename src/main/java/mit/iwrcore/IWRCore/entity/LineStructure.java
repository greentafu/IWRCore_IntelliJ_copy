package mit.iwrcore.IWRCore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LineStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lsNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineCode") // 외래 키 컬럼 이름
    @NotNull
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proplanNo") // 외래 키 컬럼 이름
    @NotNull
    private ProPlan proPlan;
}
