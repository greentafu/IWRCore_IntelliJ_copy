package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.JodalPlan;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.JodalPlanJodalChsuDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanSturctureDTO;

import java.util.List;

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
    List<JodalPlanDTO> getJodalPlanByProPlan(Long proplanNo);
    List<JodalPlanJodalChsuDTO> noneContract();
    List<JodalPlanDTO> getJodalPlanByProductMaterial(Long manuCode, Long materCode);

    // 생산부서> 생산계획에 따른 제품구성 및 수량
    List<ProPlanSturctureDTO> getStructureStock(Long proplanNo);
    // 자재부서> 조달계획 필요 자재 목록
    PageResultDTO<JodalPlanDTO, JodalPlan> nonJodalplanMaterial2(PageRequestDTO requestDTO);
    // 계약서> 계약하지 않은 조달계획 목록
    PageResultDTO<JodalPlanJodalChsuDTO, Object[]> noneContractJodalPlan(PageRequestDTO2 requestDTO2);
    // 계약서> 선택한 조달계획
    JodalPlanJodalChsuDTO selectedJodalPlan(Long joNo);
}
