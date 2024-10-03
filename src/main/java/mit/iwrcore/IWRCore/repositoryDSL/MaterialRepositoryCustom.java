package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.Material;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MaterialRepositoryCustom {
    // 자재목록
    Page<Material> findMaterialByCustomQuery(PageRequestDTO requestDTO);

    // 제품등록 중 자재들
    Page<Material> findMaterialByCustomQuery2(PageRequestDTO requestDTO);
}
