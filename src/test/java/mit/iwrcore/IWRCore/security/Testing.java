package mit.iwrcore.IWRCore.security;

import mit.iwrcore.IWRCore.entity.Contract;
import mit.iwrcore.IWRCore.repository.ContractRepository;
import mit.iwrcore.IWRCore.security.dto.multiDTO.StockDetailDTO;
import mit.iwrcore.IWRCore.security.service.ContractService;
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

    @Test
    public void test1(){

    }
}
