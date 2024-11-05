package mit.iwrcore.IWRCore.security;

import mit.iwrcore.IWRCore.entity.Member;
import mit.iwrcore.IWRCore.entity.MemberRole;
import mit.iwrcore.IWRCore.entity.Partner;
import mit.iwrcore.IWRCore.repository.MemberRepository;
import mit.iwrcore.IWRCore.repository.PartnerRepository;
import mit.iwrcore.IWRCore.repositoryDSL.PartnerRepositoryCustom;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.service.PartCodeService;
import mit.iwrcore.IWRCore.security.service.PartnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@SpringBootTest
public class MemberPartnerTests {
    @Autowired
    private MemberRepository repository;
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private PartnerService partnerService;

    @Test
    @Transactional
    public void testRead(){
        PageRequestDTO requestDTO=PageRequestDTO.builder().page(1).size(5).build();
        System.out.println(partnerService.getBaljuPartner(requestDTO));
    }
}
