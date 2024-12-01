package mit.iwrcore.IWRCore.service;

import mit.iwrcore.IWRCore.security.dto.multiDTO.ProPlanSturctureDTO;
import mit.iwrcore.IWRCore.security.service.ProplanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class ProplanServiceTests {

    @Autowired
    private ProplanService proplanService;

    @Test
    @Transactional
    public void test(){

    }
}
