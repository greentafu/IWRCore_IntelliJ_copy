package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.BaljuChasu;
import mit.iwrcore.IWRCore.security.dto.BaljuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.CalendarDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ModifyOrderDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.QuantityDateDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface BaljuChasuService {
    // 저장, 삭제
    BaljuChasuDTO saveBaljuChasu(BaljuChasuDTO baljuChasuDTO);
    void deleteBaljuChasu(Long balNo);
    BaljuChasuDTO modifyBalhuChasu(BaljuChasuDTO baljuChasuDTO);

    // 변환
    BaljuChasuDTO entityToDto(BaljuChasu entity);
    BaljuChasu dtoToEntity(BaljuChasuDTO dto);

    // 조회
    BaljuChasuDTO getBaljuChasu(Long balNo);
    List<BaljuChasuDTO> getBaljuChasuListByBaljuNo(Long baljuNo);
    List<ModifyOrderDTO> modifyBalju(Long pno);
    ModifyOrderDTO getOneBalju(Long balNo);
    List<CalendarDTO> getAllBaljuChasu();

    // 협력회사> 협력회사 메인화면 목록
    List<QuantityDateDTO> partnerMainBalju(Long baljuNo, LocalDateTime baljuDate, Long make);
}
