package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.GumsuRepository;
import mit.iwrcore.IWRCore.security.dto.BaljuDTO;
import mit.iwrcore.IWRCore.security.dto.GumsuDTO;
import mit.iwrcore.IWRCore.security.dto.JodalChasuDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.BaljuGumsuDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.BaljuJodalChasuDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GumsuServiceImpl implements GumsuService{
    private final GumsuRepository gumsuRepository;
    private final BaljuService baljuService;
    private final MemberService memberService;
    private final JodalChasuService jodalChasuService;

    @Override
    public Gumsu convertToEntity(GumsuDTO dto) {
        return Gumsu.builder()
                .gumsuNo(dto.getGumsuNo())
                .make(dto.getMake())
                .who(dto.getWho())
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO())) // MemberDTO를 Member로 변환
                .balju(baljuService.dtoToEntity(dto.getBaljuDTO()))   // BaljuDTO를 Balju로 변환
                .build();
    }

    @Override
    public GumsuDTO convertToDTO(Gumsu entity) {
        if(entity!=null){
            return GumsuDTO.builder()
                    .gumsuNo(entity.getGumsuNo())
                    .make(entity.getMake())
                    .who(entity.getWho())
                    .baljuDTO(baljuService.entityToDTO(entity.getBalju())) // Balju를 BaljuDTO로 변환
                    .memberDTO(memberService.memberTodto(entity.getWriter())) // Member를 MemberDTO로 변환
                    .build();
        }
        else return null;
    }

    @Override
    public GumsuDTO createGumsu(GumsuDTO gumsuDTO) {
        Gumsu gumsu = convertToEntity(gumsuDTO);
        Gumsu savedGumsu = gumsuRepository.save(gumsu);
        return convertToDTO(savedGumsu);
    }

    @Override
    public GumsuDTO getGumsuById(Long id) {
        List<Object[]> list= gumsuRepository.getGumsuFromBalju(id);
        Gumsu gumsu=(Gumsu) list.get(0)[0];
        return convertToDTO(gumsu);
    }

    @Override
    public GumsuDTO getGumsuByBalju(Long baljuNo){
        return convertToDTO(gumsuRepository.findGumsuByBalju(baljuNo));
    }

    @Override
    public GumsuDTO updateGumsu(Long id, GumsuDTO gumsuDTO) {
        if (!gumsuRepository.existsById(id)) {
            throw new RuntimeException("ID가 " + id + "인 GumsuDTO를 찾을 수 없습니다.");
        }
        Gumsu gumsu = convertToEntity(gumsuDTO);
        gumsu.setGumsuNo(id); // 수정할 때 ID를 설정합니다.
        Gumsu updatedGumsu = gumsuRepository.save(gumsu);
        return convertToDTO(updatedGumsu);
    }

    @Override
    public void deleteGumsu(Long id) {
        if (!gumsuRepository.existsById(id)) {
            throw new RuntimeException("ID가 " + id + "인 GumsuDTO를 찾을 수 없습니다.");
        }
        gumsuRepository.deleteById(id);
    }

    @Override
    public List<GumsuDTO> getAllGumsus() {
        return gumsuRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResultDTO<BaljuGumsuDTO, Object[]> couldGumsu(PageRequestDTO requestDTO) {
        Page<Object[]> entityPage= gumsuRepository.findGumsuByCustomQuery(requestDTO);
        return new PageResultDTO<>(entityPage, this::BaljuGumsuToDTO);
    }
    private BaljuGumsuDTO BaljuGumsuToDTO(Object[] objects){
        Balju balju=(Balju) objects[0];
        Gumsu gumsu=(Gumsu) objects[1];
        BaljuDTO baljuDTO=(balju!=null)? baljuService.entityToDTO(balju):null;
        GumsuDTO gumsuDTO=(gumsu!=null)? convertToDTO(gumsu):null;
        return new BaljuGumsuDTO(baljuDTO, gumsuDTO);
    }

    @Override
    public Long getQuantityMake(Long baljuNo){
        return gumsuRepository.quantityMake(baljuNo);
    }

    @Override
    public List<Partner> getNonGumsuPartner(){return gumsuRepository.getNonGumsuPartner();}

    @Override
    public List<BaljuJodalChasuDTO> getNoneGumsuBalju(Long pno){
        List<Object[]> list= gumsuRepository.getNoneGumsu(pno);
        List<BaljuJodalChasuDTO> dtoList=list.stream().map(this::baljuJodalChasuToDTO).toList();
        return dtoList;
    }
    private BaljuJodalChasuDTO baljuJodalChasuToDTO(Object[] objects){
        Balju balju=(Balju) objects[0];
        JodalChasu jodalChasu=(JodalChasu) objects[1];
        BaljuDTO baljuDTO=(balju!=null)?baljuService.entityToDTO(balju):null;
        JodalChasuDTO jodalChasuDTO=(jodalChasu!=null)?jodalChasuService.convertToDTO(jodalChasu):null;
        return new BaljuJodalChasuDTO(baljuDTO, jodalChasuDTO);
    }
    @Override
    public List<BaljuJodalChasuDTO> modifyGumsu(Long baljuNo){
        List<Object[]> list= gumsuRepository.modifyGumsu(baljuNo);
        List<BaljuJodalChasuDTO> dtoList=list.stream().map(this::baljuJodalChasuToDTO).toList();
        return dtoList;
    }

}