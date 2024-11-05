package mit.iwrcore.IWRCore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BaljuChasu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long balNo;
    @NotNull
    private Long balNum;
    @NotNull
    private LocalDateTime balDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "balju_id")
    @NotNull
    private Balju balju;
}
