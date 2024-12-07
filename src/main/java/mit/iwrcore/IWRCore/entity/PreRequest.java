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
@ToString
public class PreRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preReqCode;
    private Long allCheck;
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineCode")
    @NotNull
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proplan_no")
    @NotNull
    private ProPlan proPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    @NotNull
    private Member writer;

    @OneToMany(mappedBy = "preRequest")
    private List<Request> requests;
}
