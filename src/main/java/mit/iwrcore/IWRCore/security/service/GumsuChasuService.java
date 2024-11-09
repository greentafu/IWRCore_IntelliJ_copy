package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.GumsuChasu;
import mit.iwrcore.IWRCore.security.dto.GumsuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.GumsuChasuContractDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.QuantityDateDTO;

import java.util.List;

public interface GumsuChasuService {
    // 저장, 삭제
    void saveGumsuChasu(GumsuChasuDTO gumsuChasuDTO);
    void deleteGumsuChasu(Long id);
    GumsuChasuDTO modifyGumsuChasu(GumsuChasuDTO gumsuChasuDTO);

    // 변환
    GumsuChasu dtoToEntity(GumsuChasuDTO dto);
    GumsuChasuDTO entityToDTO(GumsuChasu entity);

    // 조회
    GumsuChasuDTO getGumsuChasu(Long id);


    // 검수차수> 진행도
    PageResultDTO<GumsuChasuContractDTO, Object[]> getAllGumsuChasuContract(PageRequestDTO requestDTO);
    // 검수차수> 검수차수 저장에 사용
    List<GumsuChasuDTO> getGumsuChasuByGumsu(Long gumsuNo);
    // 협력회사> 메인페이지 검수정보
    List<QuantityDateDTO> partnerMainGumsu(Long baljuNo);
}