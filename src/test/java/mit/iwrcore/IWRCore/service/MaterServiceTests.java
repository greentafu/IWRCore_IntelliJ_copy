package mit.iwrcore.IWRCore.service;

import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterCodeListDTO;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.Category.Mater.MaterLRepository;
import mit.iwrcore.IWRCore.repository.Category.Mater.MaterMRepository;
import mit.iwrcore.IWRCore.repository.Category.Mater.MaterSRepository;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterSDTO;
import mit.iwrcore.IWRCore.security.service.MaterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MaterServiceTests {
    @Autowired
    private MaterService materService;

    @Autowired
    private MaterLRepository MLRepository;
    @Autowired
    private MaterMRepository MMRepository;
    @Autowired
    private MaterSRepository MSRepository;



}
