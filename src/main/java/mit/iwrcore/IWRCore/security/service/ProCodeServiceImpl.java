package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartSDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProCodeListDTO;
import mit.iwrcore.IWRCore.repository.Category.Pro.ProLCodeRepository;
import mit.iwrcore.IWRCore.repository.Category.Pro.ProMCodeRepository;
import mit.iwrcore.IWRCore.repository.Category.Pro.ProSCodeRepository;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProSDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.CategoryPartDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.CategoryProDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProCodeServiceImpl implements ProCodeService{

    private final ProLCodeRepository lCodeRepository;
    private final ProMCodeRepository mCodeRepository;
    private final ProSCodeRepository sCodeRepository;

    // 회사 분류 삽입
    @Override
    public ProLDTO insertProL(ProLDTO dto) {
        ProL proL=proLdtoToEntity(dto);
        ProL savedL=lCodeRepository.save(proL);
        return proLTodto(savedL);
    }
    @Override
    public ProMDTO insertProM(ProMDTO dto) {
        ProM proM=proMdtoToEntity(dto);
        ProM saved=mCodeRepository.save(proM);
        return proMTodto(saved);
    }
    @Override
    public ProSDTO insertProS(ProSDTO dto) {
        ProS proS=proSdtoToEntity(dto);
        ProS saved=sCodeRepository.save(proS);
        return proSTodto(saved);
    }

    // 회사 분류 삭제
    @Override
    public void deleteProL(Long lcode) {
        lCodeRepository.deleteById(lcode);
    }
    @Override
    public void deleteProM(Long mcode) {
        mCodeRepository.deleteById(mcode);
    }
    @Override
    public void deleteProS(Long scode) {
        sCodeRepository.deleteById(scode);
    }



    // 회사 분류 가져오기
    @Override
    public ProLDTO findProL(Long lcode) {
        return proLTodto(lCodeRepository.getById(lcode));
    }
    @Override
    public ProMDTO findProM(Long mcode) {
        return proMTodto(mCodeRepository.getById(mcode));
    }
    @Override
    public ProSDTO findProS(Long scode) {
        return proSTodto(sCodeRepository.getById(scode));
    }

    // 회사 분류 리스트 가져오기
    @Override
    public List<ProLDTO> findListProL(Long type) {
        List<ProLDTO> list=new ArrayList<>();
        if(type==0L) lCodeRepository.findAll().forEach(x->list.add(proLTodto(x)));
        else lCodeRepository.getListProL1().forEach(x->list.add(proLTodto(x)));
        return list;
    }
    @Override
    public List<ProMDTO> findListProM(ProLDTO proLDTO, ProMDTO proMDTO, ProSDTO proSDTO, Long type) {
        List<ProMDTO> list=new ArrayList<>();
        Long temp=null;

        if(proSDTO!=null) temp=proSDTO.getProMDTO().getProLDTO().getProLcode();
        else if(proMDTO!=null) temp=proMDTO.getProLDTO().getProLcode();
        else if(proLDTO!=null) temp=proLDTO.getProLcode();

        if(temp==null){
            if(type==0L) mCodeRepository.findAll().forEach(x->list.add(proMTodto(x)));
            else mCodeRepository.getListProM1().forEach(x->list.add(proMTodto(x)));
        }else{
            mCodeRepository.getListProMByProL(temp).forEach(x->list.add(proMTodto(x)));
        }

        return list;
    }
    @Override
    public List<ProSDTO> findListProS(ProLDTO proLDTO, ProMDTO proMDTO, ProSDTO proSDTO, Long type) {
        List<ProSDTO> list=new ArrayList<>();
        Long temp=null;
        Long tempL=null;

        if(proLDTO!=null) tempL=proLDTO.getProLcode();
        if(proMDTO!=null) temp=proMDTO.getProMcode();
        if(proSDTO!=null) temp=proSDTO.getProMDTO().getProMcode();

        if(temp==null){
            if(tempL!=null) sCodeRepository.getListProSByProL(tempL).forEach(x->list.add(proSTodto(x)));
            else{
                if(type==0L) sCodeRepository.findAll().forEach(x->list.add(proSTodto(x)));
                else sCodeRepository.getListProS1().forEach(x->list.add(proSTodto(x)));
            }
        }else{
            sCodeRepository.getListProSByProM(temp).forEach(x->list.add(proSTodto(x)));
        }
        return list;
    }
    @Override
    public ProCodeListDTO findListProAll(ProLDTO proLDTO, ProMDTO proMDTO, ProSDTO proSDTO, Long type) {
        ProCodeListDTO list=ProCodeListDTO.builder()
                .proLDTOs(findListProL(type))
                .proMDTOs(findListProM(proLDTO, proMDTO, proSDTO, type))
                .proSDTOs(findListProS(proLDTO, proMDTO, proSDTO, type))
                .build();
        return list;
    }

    // 제품분류 페이지 가져오기
    @Override
    public PageResultDTO<CategoryProDTO, Object[]> getPagePro(Pageable pageable, Long code){
        if(code==0L){
            Page<Object[]> entityPage=lCodeRepository.getPageProL(pageable);
            return new PageResultDTO<>(entityPage, this::exProLToCategoryDTO);
        }else if(code==1L){
            Page<Object[]> entityPage=mCodeRepository.getPageProM(pageable);
            return new PageResultDTO<>(entityPage, this::exProMToCategoryDTO);
        }else{
            Page<Object[]> entityPage=sCodeRepository.getPageProS(pageable);
            return new PageResultDTO<>(entityPage, this::exProSToCategoryDTO);
        }
    }
    private CategoryProDTO exProLToCategoryDTO(Object[] objects){
        ProL proL=(ProL) objects[0];

        ProLDTO proLDTO=(proL!=null)? proLTodto(proL) : null;
        return new CategoryProDTO(proLDTO, null, null);
    }
    private CategoryProDTO exProMToCategoryDTO(Object[] objects){
        ProM proM=(ProM) objects[0];
        ProL proL=(ProL) objects[1];

        ProMDTO proMDTO=(proM!=null)? proMTodto(proM) : null;
        ProLDTO proLDTO=(proL!=null)? proLTodto(proL) : null;
        return new CategoryProDTO(proLDTO, proMDTO, null);
    }
    private CategoryProDTO exProSToCategoryDTO(Object[] objects){
        ProS proS=(ProS) objects[0];
        ProM proM=(ProM) objects[1];
        ProL proL=(ProL) objects[2];

        ProSDTO proSDTO=(proS!=null)? proSTodto(proS) : null;
        ProMDTO proMDTO=(proM!=null)? proMTodto(proM) : null;
        ProLDTO proLDTO=(proL!=null)? proLTodto(proL) : null;

        return new CategoryProDTO(proLDTO, proMDTO, proSDTO);
    }

}
