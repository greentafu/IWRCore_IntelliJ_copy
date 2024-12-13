package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Emergency;
import mit.iwrcore.IWRCore.security.dto.EmergencyDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmergencyService {
    // 저장, 삭제
    EmergencyDTO saveEmergency(EmergencyDTO emergencyDTO);
    void deleteEmergency(Long id);
    void updateEmergencyCheck(Long emerNo);

    // 변환
    Emergency dtoToEntity(EmergencyDTO dto);
    EmergencyDTO entityToDTO(Emergency entity);

    // 조회
    EmergencyDTO getEmergencyById(Long id);


    // 협력회사> 깁근납품 목록
    PageResultDTO<EmergencyDTO, Emergency> getAllEmergencies(PageRequestDTO requestDTO);
    // 협력회사> 메인화면 발주별 긴급납품 여부 확인용
    List<EmergencyDTO> getEmergencyByBalju(Long baljuNo);
    // 긴급납품> 긴급납품 목록(생산계획, 자재코드)
    PageResultDTO<EmergencyDTO, Emergency> getEmergencyByProMat(Pageable pageable, Long proplanNo, Long materCode);
}