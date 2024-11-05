package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.BaljuRepository;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO2;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.ContractBaljuDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.NewOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaljuServiceImpl implements BaljuService {

    private final BaljuRepository baljuRepository; // Balju 엔티티를 위한 리포지토리
    private final MemberService memberService; // Member 엔티티를 위한 리포지토리
    private final ContractService contractService; // Contract 엔티티를 위한 리포지토리
    private final JodalChasuService jodalChasuService;
    private final ProductService productService;

    // 추가, 삭제
    @Override
    public BaljuDTO saveBalju(BaljuDTO baljuDTO, List<BaljuChasu> baljuChasuList) {
        Balju savedBalju=null;
        if(baljuChasuList==null){
            Balju balju = dtoToEntity(baljuDTO);
            savedBalju = baljuRepository.save(balju);
        }else{
            Balju balju=baljuRepository.findById(baljuDTO.getBaljuNo())
                    .orElseThrow(IllegalArgumentException::new);
            balju.getBaljuChasus().removeAll(baljuChasuList);
            balju.setBaljuNum(baljuDTO.getBaljuNum());
            balju.setBaljuDate(baljuDTO.getBaljuDate());
            balju.setBaljuWhere(baljuDTO.getBaljuWhere());
            balju.setBaljuPlz(baljuDTO.getBaljuPlz());
            savedBalju = baljuRepository.save(balju);
        }
        return entityToDTO(savedBalju);
    }
    @Override
    public void deleteBalju(Long id) {
        baljuRepository.deleteById(id);
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
                .contract(contractService.convertToEntity(dto.getContractDTO())) // DTO를 엔티티로 변환
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
                .contractDTO(contractService.convertToDTO(entity.getContract()))
                .build();
        return baljuDTO;
    }

    // 조회
    @Override
    public BaljuDTO getBaljuById(Long id) {
        BaljuDTO baljuDTO=null;
        if(id!=null) baljuDTO=entityToDTO(baljuRepository.getById(id));
        return baljuDTO;
    }
















    @Override
    public BaljuDTO updateBalju(Long id, BaljuDTO baljuDTO) {
        if (!baljuRepository.existsById(id)) {
            throw new RuntimeException("ID가 " + id + "인 BaljuDTO를 찾을 수 없습니다.");
        }
        Balju balju = dtoToEntity(baljuDTO);
        balju.setBaljuNo(id); // 수정할 때 ID를 설정합니다.
        Balju updatedBalju = baljuRepository.save(balju);
        return entityToDTO(updatedBalju);
    }



    @Override
    public List<BaljuDTO> getAllBaljus() {
        return baljuRepository.findAll().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public PageResultDTO<BaljuDTO, Balju> finishedBalju(PageRequestDTO2 requestDTO) {
        Pageable pageable=requestDTO.getPageable(Sort.by("baljuNo").descending());
        Page<Balju> entityPage=baljuRepository.finishBalju(pageable);
        Function<Balju, BaljuDTO> fn=(entity->entityToDTO(entity));
        return new PageResultDTO<>(entityPage, fn);
    }

    @Override
    public PageResultDTO<ContractBaljuDTO, Object[]> finishedContract(PageRequestDTO2 requestDTO){
        Page<Object[]> entityPage=baljuRepository.findBaljuByCustomQuery(requestDTO);
        return new PageResultDTO<>(entityPage, this::ContractBaljuToDTO);
    }
    @Override
    public PageResultDTO<ContractBaljuDTO, Object[]> couldBalju(PageRequestDTO requestDTO) {
        Page<Object[]> entityPage=baljuRepository.findBaljuByCustomQuery2(requestDTO);
        return new PageResultDTO<>(entityPage, this::ContractBaljuToDTO);
    }
    @Override
    public PageResultDTO<ContractBaljuDTO, Object[]> finBaljuPage(PageRequestDTO2 requestDTO){
        Page<Object[]> entityPage=baljuRepository.findBaljuByCustomQuery3(requestDTO);
        return new PageResultDTO<>(entityPage, this::ContractBaljuToDTO);
    }
    private ContractBaljuDTO ContractBaljuToDTO(Object[] objects){
        Contract contract=(Contract) objects[0];
        Balju balju=(Balju) objects[1];
        ContractDTO contractDTO=contractService.convertToDTO(contract);
        BaljuDTO baljuDTO=(balju!=null)? entityToDTO(balju):null;
        return new ContractBaljuDTO(contractDTO, baljuDTO);
    }

    // 협력회사용 발주서
    @Override
    public PageResultDTO<BaljuDTO, Object[]> partnerBaljuList(PageRequestDTO requestDTO) {
        Pageable pageable=requestDTO.getPageable(Sort.by("baljuNo").descending());
        Page<Object[]> entityPage=baljuRepository.partnerBaljuList(pageable, requestDTO.getPno());
        return new PageResultDTO<>(entityPage, this::extractBaljuDTO);
    }

    @Override
    public List<BaljuDTO> partListBalju(Long pno){
        List<Object[]> entityList=baljuRepository.partListBalju(pno);
        List<BaljuDTO> dtoList=entityList.stream().map(this::extractBaljuDTO).toList();
        return dtoList;
    }
    private BaljuDTO extractBaljuDTO(Object[] objects){
        Balju balju=(Balju) objects[0];
        BaljuDTO baljuDTO=(balju!=null)?entityToDTO(balju):null;
        return baljuDTO;
    }

    @Override
    public List<NewOrderDTO> modifyBalju(Long pno){
        List<Object[]> entityList=baljuRepository.modifyBalju(pno);
        List<NewOrderDTO> newOrderDTOList=new ArrayList<>();
        Set<ContractDTO> contractDTOSet=new HashSet<>();
        Set<JodalChasuDTO> jodalChasuDTOSet=new HashSet<>();
        Set<BaljuDTO> baljuDTOSet=new HashSet<>();

        for(Object[] objects:entityList){
            Contract contract=(Contract) objects[0];
            JodalChasu jodalChasu=(JodalChasu) objects[1];
            Balju balju=(Balju) objects[2];
            ContractDTO contractDTO=(contract!=null)?contractService.convertToDTO(contract):null;
            JodalChasuDTO jodalChasuDTO=(jodalChasu!=null)?jodalChasuService.convertToDTO(jodalChasu):null;
            BaljuDTO baljuDTO=(balju!=null)?entityToDTO(balju):null;
            contractDTOSet.add(contractDTO);
            jodalChasuDTOSet.add(jodalChasuDTO);
            baljuDTOSet.add(baljuDTO);

            if(jodalChasuDTOSet.size()==3){
                ContractDTO saveContractDTO=contractDTOSet.stream().toList().get(0);
                List<JodalChasuDTO> saveJodalChasuDTOList=jodalChasuDTOSet.stream().toList();
                List<JodalChasuDTO> sortedJodalChsasuList=saveJodalChasuDTOList.stream()
                        .sorted(Comparator.comparing(JodalChasuDTO::getJcnum))
                        .collect(Collectors.toList());
                BaljuDTO saveBaljuDTO=baljuDTOSet.stream().toList().get(0);

                NewOrderDTO newOrderDTO=new NewOrderDTO(saveContractDTO, sortedJodalChsasuList, saveBaljuDTO);
                newOrderDTOList.add(newOrderDTO);
                contractDTOSet.clear();
                jodalChasuDTOSet.clear();
                baljuDTOSet.clear();
            }
        }
        return newOrderDTOList;
    }

    @Override
    public List<ProductDTO> baljuProduct(){
        List<Object[]> entityList=baljuRepository.baljuProductList();
        List<ProductDTO> dtoList=entityList.stream().map(this::ObjectToProductDTO).toList();
        return dtoList;
    }
    private ProductDTO ObjectToProductDTO(Object[] objects){
        Product product=(Product) objects[0];
        ProductDTO productDTO=(product!=null)?productService.entityToDto(product):null;
        return productDTO;
    }
}