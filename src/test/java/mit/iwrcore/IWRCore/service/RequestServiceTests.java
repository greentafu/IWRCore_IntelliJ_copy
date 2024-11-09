package mit.iwrcore.IWRCore.service;

import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.repository.RequestRepository;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.RequestDTO;
import mit.iwrcore.IWRCore.security.service.MaterialService;
import mit.iwrcore.IWRCore.security.service.MemberService;
import mit.iwrcore.IWRCore.security.service.ProplanService;
import mit.iwrcore.IWRCore.security.service.RequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
public class RequestServiceTests {
    @Autowired
    private RequestService requestService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private ProplanService proplanService;
    @Autowired
    private RequestRepository requestRepository;



}