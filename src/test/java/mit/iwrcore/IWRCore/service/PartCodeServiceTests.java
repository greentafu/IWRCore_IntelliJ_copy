package mit.iwrcore.IWRCore.service;

import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartCodeListDTO;
import mit.iwrcore.IWRCore.entity.PartL;
import mit.iwrcore.IWRCore.entity.PartM;
import mit.iwrcore.IWRCore.entity.PartS;
import mit.iwrcore.IWRCore.repository.Category.Part.PartLCodeRepository;
import mit.iwrcore.IWRCore.repository.Category.Part.PartMCodeRepository;
import mit.iwrcore.IWRCore.repository.Category.Part.PartSCodeRepository;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartSDTO;
import mit.iwrcore.IWRCore.security.service.PartCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class PartCodeServiceTests {
    @Autowired
    private PartCodeService partCodeService;

    @Autowired
    private PartLCodeRepository lCodeRepository;
    @Autowired
    private PartMCodeRepository mCodeRepository;
    @Autowired
    private PartSCodeRepository sCodeRepository;


}
