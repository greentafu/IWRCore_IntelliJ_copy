package mit.iwrcore.IWRCore.service;

import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.Balju;
import mit.iwrcore.IWRCore.entity.Member;
import mit.iwrcore.IWRCore.repository.BaljuRepository;
import mit.iwrcore.IWRCore.repository.EmergencyRepository;
import mit.iwrcore.IWRCore.repository.MemberRepository;
import mit.iwrcore.IWRCore.security.dto.EmergencyDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.service.BaljuService;
import mit.iwrcore.IWRCore.security.service.EmergencyService;
import mit.iwrcore.IWRCore.security.service.EmergencyServiceImpl;
import mit.iwrcore.IWRCore.security.service.MemberService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;

import java.time.LocalDateTime;

@Nested
@SpringBootTest
class EmergencyServiceTests {

    @Autowired
    private EmergencyServiceImpl emergencyimpl;

    @Autowired
    private EmergencyRepository emergencyRepository;

    @Autowired
    private BaljuRepository baljuRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BaljuService baljuService;
    @Autowired
    private EmergencyService emergencyService;


}

