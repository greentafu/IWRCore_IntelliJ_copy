package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.JodalPlan;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.JodalPlanJodalChsuDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanSturctureDTO;

import java.util.List;
import java.util.Objects;

public interface JodalPlanService {
    // 저장, 삭제
    void saveJodalPlan(JodalPlanDTO dto);
    void saveJodalPlanFromProplan(ProplanDTO proplanDTO, MemberDTO memberDTO);
    void deleteJodalPlan(Long id);

    // 변환
    JodalPlan dtoToEntity(JodalPlanDTO dto);
    JodalPlanDTO entityToDTO(JodalPlan entity);

    // 조회
    JodalPlanDTO getJodalPlan(Long id);
    Long newNoneJodalChasuCount();
    List<JodalPlanDTO> findJodalPlanByProPlan(Long proplanNo);
    List<JodalPlanJodalChsuDTO> noneContract();


    // 자재팀> 조달계획 필요 자재 목록
    PageResultDTO<JodalPlanDTO, JodalPlan> nonJodalplanMaterial2(PageRequestDTO requestDTO);
    // 계약서> 계약하지 않은 조달계획 목록
    PageResultDTO<JodalPlanJodalChsuDTO, Object[]> noneContractJodalPlan(PageRequestDTO2 requestDTO2);
    // 계약서> 선택한 조달계획
    JodalPlanJodalChsuDTO selectedJodalPlan(Long joNo);
}
