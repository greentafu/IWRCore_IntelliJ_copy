package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.BaljuChasu;
import mit.iwrcore.IWRCore.security.dto.BaljuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ModifyOrderDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.NewOrderDTO;

import java.util.List;

public interface BaljuChasuService {
    // 추가, 삭제
    BaljuChasuDTO saveBaljuChasu(BaljuChasuDTO baljuChasuDTO);
    void deleteBaljuChasu(Long balNo);
    // 변환
    BaljuChasuDTO entityToDto(BaljuChasu entity);
    BaljuChasu dtoToEntity(BaljuChasuDTO dto);
    // 조회
    BaljuChasuDTO getBaljuChasuById(Long balNo);
    List<BaljuChasuDTO> getBaljuChasuListByBaljuNo(Long baljuNo);
    List<ModifyOrderDTO> modifyBalju(Long pno);
}
