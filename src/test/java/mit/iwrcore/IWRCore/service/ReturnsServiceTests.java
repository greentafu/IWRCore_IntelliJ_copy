package mit.iwrcore.IWRCore.service;

import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.Returns;
import mit.iwrcore.IWRCore.entity.Shipment;
import mit.iwrcore.IWRCore.repository.ReturnsRepository;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.ReturnsDTO;
import mit.iwrcore.IWRCore.security.service.MemberService;
import mit.iwrcore.IWRCore.security.service.ReturnsService;
import mit.iwrcore.IWRCore.security.service.ShipmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ReturnsServiceTests {
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ReturnsService returnsService;
    @Autowired
    private ReturnsRepository returnsRepository;


}
