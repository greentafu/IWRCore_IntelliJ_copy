package mit.iwrcore.IWRCore.service;


import mit.iwrcore.IWRCore.repository.Category.Part.PartLCodeRepository;
import mit.iwrcore.IWRCore.repository.Category.Part.PartMCodeRepository;
import mit.iwrcore.IWRCore.repository.Category.Part.PartSCodeRepository;
import mit.iwrcore.IWRCore.security.service.PartCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;

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
