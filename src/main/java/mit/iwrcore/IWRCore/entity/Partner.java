package mit.iwrcore.IWRCore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "partS")
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;  // 파트너 번호
    @NotNull
    private String name;  // 이름
    @NotNull
    private String registrationNumber;  // 등록번호
    @NotNull
    private String location;  // 위치
    @NotNull
    private String type;  // 유형
    @NotNull
    private String sector;  // 업종
    @NotNull
    private String ceo;  // CEO
    @NotNull
    private String telNumber;  // 전화번호
    private String faxNumber;  // 팩스번호
    private String email;  // 이메일
    @NotNull
    private String contacter;  // 담당자
    @NotNull
    private String contacterNumber;  // 담당자 연락처
    @NotNull
    private String contacterEmail;  // 담당자 이메일
    private String id;  // ID
    @NotNull
    private String pw;  // 비밀번호
    @NotNull
    private String password;  // 비밀번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partScode")
    @NotNull
    private PartS partS;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    @PostPersist
    public void generateId() {
        if (this.id==null || this.id.isEmpty() || this.id=="") {
            String temp = "partner" + pno + "_" + registrationNumber.substring(7);
            this.id = temp;
        }
    }

    public void setPartnerRole(MemberRole partnerRole) {
        if (roleSet == null || this.id.isEmpty()) {
            roleSet = new HashSet<>();
        }
        roleSet.clear();
        roleSet.add(partnerRole);
    }
}
