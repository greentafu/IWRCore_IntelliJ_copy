package mit.iwrcore.IWRCore.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.Line;
import mit.iwrcore.IWRCore.entity.Plan;
import mit.iwrcore.IWRCore.entity.Product;
import mit.iwrcore.IWRCore.repository.LineRepository;
import mit.iwrcore.IWRCore.repository.PlanRepository;
import mit.iwrcore.IWRCore.repository.ProductRepository;
import mit.iwrcore.IWRCore.security.dto.PlanDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class PlanServiceImpl implements PlanService{
    private final PlanRepository planRepository;
    private final ProductServiceImpl productServiceImpl;

    private final LineService lineService;

    @Override
    public void saveLine(PlanDTO dto) {
        Plan plan = dtoToEntity(dto);
        planRepository.save(plan);
    }
    @Override
    public void deleteById(Long id) {
        planRepository.deleteById(id);
    }
    @Override
    public PlanDTO findLineByLine(Long manuCode, String line){
        Plan plan=planRepository.findLineByLine(manuCode, line);
        PlanDTO planDTO=entityToDTO(plan);
        return planDTO;
    }

    @Override
    public List<PlanDTO> findByProductId(Long productId) {
        List<Plan> plans = planRepository.findByProduct_ManuCode(productId);
        List<PlanDTO> dtoList=plans.stream().map(this::entityToDTO).toList();
        return dtoList;
    }
    private PlanDTO exPlan(Object[] objects){
        Plan plan=(Plan) objects[0];
        PlanDTO planDTO=(plan!=null)? entityToDTO(plan):null;
        return planDTO;
    }


    @Override
    public Plan dtoToEntity(PlanDTO dto) {
        if(dto==null) return null;
        Plan entity=Plan.builder()
                .plancode(dto.getPlancode())
                .line(lineService.stringToLine(dto.getLine()))
                .product(productServiceImpl.productDtoToEntity(dto.getProductDTO()))
                .quantity(dto.getQuantity())
                .build();
        return entity;
    }
    @Override
    public PlanDTO entityToDTO(Plan entity) {
        if(entity==null) return null;
        PlanDTO dto=PlanDTO.builder()
                .plancode(entity.getPlancode())
                .line(lineService.lineToString(entity.getLine()))
                .productDTO(productServiceImpl.productEntityToDto(entity.getProduct()))
                .quantity(entity.getQuantity())
                .build();
        return dto;
    }
}