package mit.iwrcore.IWRCore.service;

import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.Invoice;
import mit.iwrcore.IWRCore.entity.Member;
import mit.iwrcore.IWRCore.entity.Returns;
import mit.iwrcore.IWRCore.entity.Shipment;
import mit.iwrcore.IWRCore.repository.InvoiceRepository;
import mit.iwrcore.IWRCore.repository.ReturnsRepository;
import mit.iwrcore.IWRCore.repository.ShipmentRepository;
import mit.iwrcore.IWRCore.security.dto.InvoiceDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.ShipmentDTO;
import mit.iwrcore.IWRCore.security.service.BaljuService;
import mit.iwrcore.IWRCore.security.service.InvoiceService;
import mit.iwrcore.IWRCore.security.service.MemberService;
import mit.iwrcore.IWRCore.security.service.ShipmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;

import java.time.LocalDateTime;

@SpringBootTest
public class ShipmentServiceTests {
    @Autowired
    private BaljuService baljuService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ShipmentRepository shipmentRepository;
    @Autowired
    private ReturnsRepository returnsRepository;

}



