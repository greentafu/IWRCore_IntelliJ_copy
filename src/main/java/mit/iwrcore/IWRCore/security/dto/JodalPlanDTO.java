package mit.iwrcore.IWRCore.security.dto;

import jakarta.persistence.*;
import lombok.*;
import mit.iwrcore.IWRCore.entity.Contract;
import mit.iwrcore.IWRCore.entity.JodalChasu;
import mit.iwrcore.IWRCore.entity.Member;
import mit.iwrcore.IWRCore.entity.ProPlan;

import java.time.LocalDateTime;
import java.util.List;
@Setter
@Getter
@Builder
@ToString
public class JodalPlanDTO {
    private Long joNo;
    private LocalDateTime planDate;
    private MemberDTO memberDTO;
    private ProplanDTO proplanDTO;
    private MaterialDTO materialDTO;
}

