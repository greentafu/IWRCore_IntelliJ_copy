package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Plan;
import mit.iwrcore.IWRCore.security.dto.LineDTO;
import mit.iwrcore.IWRCore.security.dto.PlanDTO;

import java.util.List;

public interface PlanService {
    // 저장, 삭제
    void saveLine(PlanDTO dto);
    void deleteLine(Long id);

    // 변환
    Plan dtoToEntity(PlanDTO dto);
    PlanDTO entityToDTO(Plan entity);

    // 조회
    PlanDTO getLineByLine(Long manuCode, LineDTO line);
    List<PlanDTO> getLineByProduct(Long productId);
}
