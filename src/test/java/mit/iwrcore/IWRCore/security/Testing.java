package mit.iwrcore.IWRCore.security;

import jakarta.transaction.Transactional;
import mit.iwrcore.IWRCore.entity.Contract;
import mit.iwrcore.IWRCore.entity.Request;
import mit.iwrcore.IWRCore.repository.ContractRepository;
import mit.iwrcore.IWRCore.security.dto.multiDTO.CalendarDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.StockDetailDTO;
import mit.iwrcore.IWRCore.security.service.ContractService;
import mit.iwrcore.IWRCore.security.service.RequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class Testing {
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    ContractService contractService;
    @Autowired
    RequestService requestService;

    @Test
    @Transactional
    public void test1(){
        List<CalendarDTO> a=requestService.getAllRequest();
        a.forEach(x-> System.out.println("@@ "+x.getTitle()+"/"+x.getStartDate()+"/"+x.getEndDate()));
    }
}
