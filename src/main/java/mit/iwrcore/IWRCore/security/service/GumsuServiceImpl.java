package mit.iwrcore.IWRCore.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.GumsuRepository;
import mit.iwrcore.IWRCore.security.dto.BaljuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.BaljuDTO;
import mit.iwrcore.IWRCore.security.dto.GumsuDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.BaljuBaljuChasuDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.BaljuGumsuDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GumsuServiceImpl implements GumsuService{
    private final GumsuRepository gumsuRepository;
    private final BaljuService baljuService;
    private final MemberService memberService;
    private final BaljuChasuService baljuChasuService;

    // 저장, 삭제
    @Override
    public GumsuDTO saveGumsu(GumsuDTO gumsuDTO) {
        Gumsu savedGumsu = gumsuRepository.save(dtoToEntity(gumsuDTO));
        return entityToDTO(savedGumsu);
    }
    @Override
    public void deleteGumsu(Long id) {
        gumsuRepository.deleteById(id);
    }
    @Override
    public GumsuDTO modifyGumsu(GumsuDTO gumsuDTO){
        Gumsu gumsu=gumsuRepository.findById(gumsuDTO.getGumsuNo()).orElseThrow(() -> new EntityNotFoundException("Gumsu not found"));
        gumsu.setMake(gumsuDTO.getMake());
        Gumsu savedGumsu=gumsuRepository.save(gumsu);
        return entityToDTO(savedGumsu);
    }

    // 변환
    @Override
    public Gumsu dtoToEntity(GumsuDTO dto) {
        if(dto==null) return null;
        return Gumsu.builder()
                .gumsuNo(dto.getGumsuNo())
                .make(dto.getMake())
                .who(dto.getWho())
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO()))
                .balju(baljuService.dtoToEntity(dto.getBaljuDTO()))
                .build();
    }
    @Override
    public GumsuDTO entityToDTO(Gumsu entity) {
        if(entity==null) return null;
        return GumsuDTO.builder()
                .gumsuNo(entity.getGumsuNo())
                .make(entity.getMake())
                .who(entity.getWho())
                .baljuDTO(baljuService.entityToDTO(entity.getBalju()))
                .memberDTO(memberService.memberTodto(entity.getWriter()))
                .build();
    }

    // 조회
    @Override
    public GumsuDTO getGumsu(Long id) {
        Gumsu gumsu=gumsuRepository.findById(id).get();
        return entityToDTO(gumsu);
    }
    @Override
    public GumsuDTO getGumsuByBalju(Long baljuNo){
        Gumsu gumsu=gumsuRepository.getGumsuByBaljuNo(baljuNo);
        if(gumsu==null) return null;
        return entityToDTO(gumsu);
    }


    // 검수차수> 검수해야할 목록
    @Override
    public PageResultDTO<BaljuGumsuDTO, Object[]> couldGumsu(PageRequestDTO requestDTO) {
        Page<Object[]> entityPage= gumsuRepository.findGumsuByCustomQuery(requestDTO);
        return new PageResultDTO<>(entityPage, this::BaljuGumsuToDTO);
    }
    private BaljuGumsuDTO BaljuGumsuToDTO(Object[] objects){
        Balju balju=(Balju) objects[0];
        Gumsu gumsu=(Gumsu) objects[1];
        BaljuDTO baljuDTO=(balju!=null)? baljuService.entityToDTO(balju):null;
        GumsuDTO gumsuDTO=(gumsu!=null)? entityToDTO(gumsu):null;
        return new BaljuGumsuDTO(baljuDTO, gumsuDTO);
    }
    // 검수차수> 회사별 검수차수 설정 가능 발주 목록
    @Override
    public List<BaljuBaljuChasuDTO> getNoneGumsuBalju(Long pno){
        List<Object[]> list= gumsuRepository.getNoneGumsu(pno);
        List<BaljuBaljuChasuDTO> dtoList=list.stream().map(this::baljuBaljuChasuToDTO).toList();
        return dtoList;
    }
    private BaljuBaljuChasuDTO baljuBaljuChasuToDTO(Object[] objects){
        Balju balju=(Balju) objects[0];
        BaljuChasu baljuChasu=(BaljuChasu) objects[1];
        BaljuDTO baljuDTO=(balju!=null)?baljuService.entityToDTO(balju):null;
        BaljuChasuDTO baljuChasuDTO=(baljuChasu!=null)?baljuChasuService.entityToDto(baljuChasu):null;
        return new BaljuBaljuChasuDTO(baljuDTO, baljuChasuDTO);
    }
}