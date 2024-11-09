package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.Plan;
import mit.iwrcore.IWRCore.repository.PlanRepository;
import mit.iwrcore.IWRCore.security.dto.PlanDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PlanServiceImpl implements PlanService{
    private final PlanRepository planRepository;
    private final ProductServiceImpl productServiceImpl;
    private final LineService lineService;

    // 저장, 삭제
    @Override
    public void saveLine(PlanDTO dto) {
        Plan plan = dtoToEntity(dto);
        planRepository.save(plan);
    }
    @Override
    public void deleteLine(Long id) {
        planRepository.deleteById(id);
    }

    // 변환
    @Override
    public Plan dtoToEntity(PlanDTO dto) {
        if(dto==null) return null;
        return Plan.builder()
                .plancode(dto.getPlancode())
                .line(lineService.stringToLine(dto.getLine()))
                .product(productServiceImpl.dtoToEntity(dto.getProductDTO()))
                .quantity(dto.getQuantity())
                .build();
    }
    @Override
    public PlanDTO entityToDTO(Plan entity) {
        if(entity==null) return null;
        return PlanDTO.builder()
                .plancode(entity.getPlancode())
                .line(lineService.lineToString(entity.getLine()))
                .productDTO(productServiceImpl.entityToDto(entity.getProduct()))
                .quantity(entity.getQuantity())
                .build();
    }

    // 조회
    @Override
    public PlanDTO getLineByLine(Long manuCode, String line){
        Plan plan=planRepository.findLineByLine(manuCode, line);
        return entityToDTO(plan);
    }
    @Override
    public List<PlanDTO> getLineByProduct(Long productId) {
        List<Plan> plans = planRepository.findPlanByProduct(productId);
        return plans.stream().map(this::entityToDTO).toList();
    }
}