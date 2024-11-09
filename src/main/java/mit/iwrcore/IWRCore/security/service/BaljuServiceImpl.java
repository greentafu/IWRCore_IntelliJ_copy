package mit.iwrcore.IWRCore.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.BaljuRepository;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ContractBaljuDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BaljuServiceImpl implements BaljuService {
    private final BaljuRepository baljuRepository;
    private final MemberService memberService;
    private final ContractService contractService;
    private final ProductService productService;

    // 저장, 삭제
    @Override
    public BaljuDTO saveBalju(BaljuDTO baljuDTO) {
        Balju savedBalju = baljuRepository.save(dtoToEntity(baljuDTO));
        return entityToDTO(savedBalju);
    }
    @Override
    public void deleteBalju(Long id) {
        baljuRepository.deleteById(id);
    }
    @Override
    public BaljuDTO modifyBalju(BaljuDTO baljuDTO){
        Balju balju=baljuRepository.findById(baljuDTO.getBaljuNo()).orElseThrow(() -> new EntityNotFoundException("Balju not found"));
        balju.setBaljuNum(baljuDTO.getBaljuNum());
        balju.setBaljuDate(baljuDTO.getBaljuDate());
        balju.setBaljuWhere(baljuDTO.getBaljuWhere());
        balju.setBaljuPlz(baljuDTO.getBaljuPlz());
        Balju savedBalju=baljuRepository.save(balju);
        return entityToDTO(savedBalju);
    }

    // 변환
    @Override
    public Balju dtoToEntity(BaljuDTO dto) {
        Balju balju=null;
        if(dto!=null) balju=Balju.builder()
                .baljuNo(dto.getBaljuNo())
                .baljuNum(dto.getBaljuNum())
                .baljuDate(dto.getBaljuDate())
                .baljuWhere(dto.getBaljuWhere())
                .baljuPlz(dto.getBaljuPlz())
                .finCheck(dto.getFinCheck())
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO())) // DTO를 엔티티로 변환
                .contract(contractService.dtoToEntity(dto.getContractDTO())) // DTO를 엔티티로 변환
                .build();
        return balju;
    }
    @Override
    public BaljuDTO entityToDTO(Balju entity) {
        BaljuDTO baljuDTO=null;
        if(entity!=null) baljuDTO=BaljuDTO.builder()
                .baljuNo(entity.getBaljuNo())
                .baljuNum(entity.getBaljuNum())
                .baljuDate(entity.getBaljuDate())
                .baljuWhere(entity.getBaljuWhere())
                .baljuPlz(entity.getBaljuPlz())
                .finCheck(entity.getFinCheck())
                .regDate(entity.getRegDate())
                .memberDTO(memberService.memberTodto(entity.getWriter()))
                .contractDTO(contractService.entityToDTO(entity.getContract()))
                .build();
        return baljuDTO;
    }

    // 조회
    @Override
    public BaljuDTO getBalju(Long id) {
        if(id==null) return null;
        return entityToDTO(baljuRepository.findById(id).get());
    }


    // 계약서> 계약 완료 목록
    @Override
    public PageResultDTO<ContractBaljuDTO, Object[]> finishedContract(PageRequestDTO2 requestDTO){
        Page<Object[]> entityPage=baljuRepository.findBaljuByCustomQuery(requestDTO);
        return new PageResultDTO<>(entityPage, this::ContractBaljuToDTO);
    }
    // 발주서> 발주 해야하는 계약 목록
    @Override
    public PageResultDTO<ContractBaljuDTO, Object[]> couldBalju(PageRequestDTO requestDTO) {
        Page<Object[]> entityPage=baljuRepository.findBaljuByCustomQuery2(requestDTO);
        return new PageResultDTO<>(entityPage, this::ContractBaljuToDTO);
    }
    // 발주서> 발주완료한 목록
    @Override
    public PageResultDTO<ContractBaljuDTO, Object[]> finBaljuPage(PageRequestDTO2 requestDTO){
        Page<Object[]> entityPage=baljuRepository.findBaljuByCustomQuery3(requestDTO);
        return new PageResultDTO<>(entityPage, this::ContractBaljuToDTO);
    }
    private ContractBaljuDTO ContractBaljuToDTO(Object[] objects){
        Contract contract=(Contract) objects[0];
        Balju balju=(Balju) objects[1];
        ContractDTO contractDTO=contractService.entityToDTO(contract);
        BaljuDTO baljuDTO=(balju!=null)? entityToDTO(balju):null;
        return new ContractBaljuDTO(contractDTO, baljuDTO);
    }
    // 발주서> 협력회사별 발주 가능한 목록
    @Override
    public List<BaljuDTO> partListBalju(Long pno){
        List<Balju> entityList=baljuRepository.partListBalju(pno);
        return entityList.stream().map(this::entityToDTO).toList();
    }
    // 협력회사> 협력회사용 발주서 목록
    @Override
    public PageResultDTO<BaljuDTO, Balju> partnerBaljuList(PageRequestDTO requestDTO) {
        Pageable pageable=requestDTO.getPageable(Sort.by("baljuNo").descending());
        Page<Balju> entityPage=baljuRepository.partnerBaljuList(pageable, requestDTO.getPno());
        Function<Balju, BaljuDTO> fn = (entity -> entityToDTO(entity));
        return new PageResultDTO<>(entityPage, fn);
    }
    // 메인화면> 발주 중인 제품 목록
    @Override
    public List<ProductDTO> baljuProduct(){
        List<Product> entityList=baljuRepository.baljuProductList();
        List<ProductDTO> dtoList=new ArrayList<>();
        entityList.stream().forEach(x->dtoList.add(productService.entityToDto(x)));
        return dtoList;
    }
}