package mit.iwrcore.IWRCore.service;


import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.repository.ContractRepository;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;

import java.time.LocalDateTime;


@SpringBootTest
public class ContractServiceTests {
    @Autowired
    private ContractService contractService;
    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JodalPlanService jodalPlanService;

    @Autowired
    private PartnerService partnerService;

    @Test
    @Transactional
    public void test(){
        PageRequestDTO requestDTO=PageRequestDTO.builder()
                .size(15).page(1).materCode(1L).selectedYear(2023)
                .build();
        System.out.println("@@@@@@@@@ "+contractService.stockdetailList(requestDTO));
    }

}
