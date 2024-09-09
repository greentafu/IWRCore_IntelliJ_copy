package mit.iwrcore.IWRCore.repositoryDSL;

import mit.iwrcore.IWRCore.entity.Material;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MaterialRepositoryCustom {
    Page<Material> findMaterialByCustomQuery(PageRequestDTO requestDTO);
}
