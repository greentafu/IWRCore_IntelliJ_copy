package mit.iwrcore.IWRCore.service;

import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.repository.BoxRepository;
import mit.iwrcore.IWRCore.repository.Category.Mater.MaterSRepository;
import mit.iwrcore.IWRCore.repository.MaterialRepository;
import mit.iwrcore.IWRCore.security.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
public class MaterialServiceTests {
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private BoxRepository boxRepository;
    @Autowired
    private MaterSRepository materSRepository;

    @Autowired
    private MemberService memberService;
    @Autowired
    private MaterService materService;
    @Autowired
    private FileService fileService;

    @Autowired
    private MaterialServiceImpl materialServiceImpl;

    @Test
    @Transactional
    @Commit
    public void insertMaterial() {
        System.out.println(fileService.getMaterialFileList(1L));
    }
}
