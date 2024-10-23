package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Line;
import mit.iwrcore.IWRCore.entity.Plan;
import mit.iwrcore.IWRCore.entity.ProPlan;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.security.dto.PlanDTO;

import java.util.List;

public interface PlanService {

    void saveLine(PlanDTO dto);
    void deleteById(Long id);
    PlanDTO findLineByLine(Long manuCode, String line);
    List<PlanDTO> findByProductId(Long productId);

    Plan dtoToEntity(PlanDTO dto);
    PlanDTO entityToDTO(Plan entity);
}
