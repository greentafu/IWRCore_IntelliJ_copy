package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PreRequestRepositoryCustom {
    // 출하요청 출하목록
    Page<Object[]> requestPage(PageRequestDTO requestDTO);
    // 출하요청> 생산계획에 따른 제품 구성 및 재고(수정)
    List<Object[]> getStructureStock(Long preReqCode);
}
