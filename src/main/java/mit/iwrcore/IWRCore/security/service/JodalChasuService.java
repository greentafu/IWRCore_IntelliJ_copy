package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.JodalChasu;
import mit.iwrcore.IWRCore.security.dto.JodalChasuDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanSturctureDTO;

import java.util.List;

public interface JodalChasuService {
    // 저장, 삭제
    JodalChasuDTO saveJodalChasu(JodalChasuDTO dto);
    void deleteJodalChasu(Long id);
    void deleteJodalChasuByPlan(Long joNo);

    // 변환
    JodalChasu dtoToEntity(JodalChasuDTO dto);
    JodalChasuDTO entityToDTO(JodalChasu entity);

    // 조회
    List<JodalChasuDTO> getJodalChasuByJodalPlan(Long joNo);


    // 조달계획> 조달차수 리스트
    List<ProPlanSturctureDTO> modifyJodalChasu(Long proplanNo);
}
