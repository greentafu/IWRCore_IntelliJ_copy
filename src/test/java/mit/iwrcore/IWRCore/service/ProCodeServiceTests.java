package mit.iwrcore.IWRCore.service;

import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProCodeListDTO;
import mit.iwrcore.IWRCore.entity.ProL;
import mit.iwrcore.IWRCore.entity.ProM;
import mit.iwrcore.IWRCore.entity.ProS;
import mit.iwrcore.IWRCore.repository.Category.Pro.ProLCodeRepository;
import mit.iwrcore.IWRCore.repository.Category.Pro.ProMCodeRepository;
import mit.iwrcore.IWRCore.repository.Category.Pro.ProSCodeRepository;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProSDTO;
import mit.iwrcore.IWRCore.security.service.ProCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ProCodeServiceTests {
    @Autowired
    private ProCodeService proCodeService;
    @Autowired
    private ProLCodeRepository lCodeRepository;
    @Autowired
    private ProMCodeRepository mCodeRepository;
    @Autowired
    private ProSCodeRepository sCodeRepository;



}
