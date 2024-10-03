package mit.iwrcore.IWRCore.service;

import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.Box;
import mit.iwrcore.IWRCore.entity.MaterS;
import mit.iwrcore.IWRCore.entity.Material;
import mit.iwrcore.IWRCore.repository.BoxRepository;
import mit.iwrcore.IWRCore.repository.Mater.MaterSRepository;
import mit.iwrcore.IWRCore.repository.MaterialRepository;
import mit.iwrcore.IWRCore.security.dto.BoxDTO;
import mit.iwrcore.IWRCore.security.dto.MaterDTO.MaterSDTO;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.List;

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
    private BoxService boxService;

    @Autowired
    private MaterialServiceImpl materialServiceImpl;

    @Test
    @Transactional
    @Commit
    public void insertMaterial() {
        PageRequestDTO requestDTO=new PageRequestDTO();
        List<Long> longs=new ArrayList<>();
        longs.add(1L); longs.add(2L);
        requestDTO.setMaterials(longs);
        System.out.println(materialService.productMaterialList(requestDTO));
    }
}
