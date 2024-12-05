package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.entity.Gumsu;
import mit.iwrcore.IWRCore.security.dto.GumsuDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.BaljuBaljuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.BaljuGumsuDTO;

import java.util.List;

public interface GumsuService {
    // 저장, 삭제
    GumsuDTO saveGumsu(GumsuDTO gumsuDTO);
    void deleteGumsu(Long id);
    GumsuDTO modifyGumsu(GumsuDTO gumsuDTO);

    // 변환
    Gumsu dtoToEntity(GumsuDTO dto);
    GumsuDTO entityToDTO(Gumsu entity);

    // 조회
    GumsuDTO getGumsu(Long id);
    GumsuDTO getGumsuByBalju(Long baljuNo);


    // 검수차수> 검수해야할 목록
    PageResultDTO<BaljuGumsuDTO, Object[]> couldGumsu(PageRequestDTO requestDTO);
    // 검수차수> 회사별 검수차수 설정 가능 발주 목록
    List<BaljuBaljuChasuDTO> getNoneGumsuBalju(Long pno);
}