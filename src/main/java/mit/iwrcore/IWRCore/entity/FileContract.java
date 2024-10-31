package mit.iwrcore.IWRCore.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileContract {
    @Id
    private String uuid;
    private String uploadPath;
    private String fileName;
    private String contentType;
    private int imgType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract")
    private Contract contract;
}
