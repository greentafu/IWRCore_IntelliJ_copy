package mit.iwrcore.IWRCore.service;

import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.GumsuChasu;
import mit.iwrcore.IWRCore.repository.GumsuChasuRepository;
import mit.iwrcore.IWRCore.security.dto.GumsuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.GumsuDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.service.GumsuChasuService;
import mit.iwrcore.IWRCore.security.service.GumsuService;
import mit.iwrcore.IWRCore.security.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class GumsuChasuTests {
    @Autowired
    private MemberService memberService;
    @Autowired
    private GumsuService gumsuService;
    @Autowired
    private GumsuChasuService gumsuChasuService;
    @Autowired
    private GumsuChasuRepository gumsuChasuRepository;


}
