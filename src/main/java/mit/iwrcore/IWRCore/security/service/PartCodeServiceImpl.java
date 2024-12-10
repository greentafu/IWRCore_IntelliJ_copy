package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartCodeListDTO;
import mit.iwrcore.IWRCore.repository.Category.Part.PartLCodeRepository;
import mit.iwrcore.IWRCore.repository.Category.Part.PartMCodeRepository;
import mit.iwrcore.IWRCore.repository.Category.Part.PartSCodeRepository;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.PartDTO.PartSDTO;
import mit.iwrcore.IWRCore.security.dto.ContractDTO;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.CategoryPartDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.StockQuantityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class PartCodeServiceImpl implements PartCodeService{

    private final PartLCodeRepository lCodeRepository;
    private final PartMCodeRepository mCodeRepository;
    private final PartSCodeRepository sCodeRepository;

    // 회사 분류 삽입
    @Override
    public PartLDTO insertPartL(PartLDTO dto) {
        log.info("협력회사 대분류 삽입");
        PartL partL=partLdtoToEntity(dto);
        PartL saved=lCodeRepository.save(partL);
        return partLTodto(saved);
    }
    @Override
    public PartMDTO insertPartM(PartMDTO dto) {
        log.info("협력회사 중분류 삽입");
        PartM partM=partMdtoToEntity(dto);
        PartM saved=mCodeRepository.save(partM);
        return partMTOdto(saved);
    }
    @Override
    public PartSDTO insertPartS(PartSDTO dto) {
        log.info("협력회사 소분류 삽입");
        PartS partS=partSdtoToEntity(dto);
        PartS saved=sCodeRepository.save(partS);
        return partSTodto(saved);
    }

    // 회사 분류 삭제
    @Override
    public void deletePartL(Long lcode) {
        lCodeRepository.deleteById(lcode);
    }
    @Override
    public void deletePartM(Long mcode) {
        mCodeRepository.deleteById(mcode);
    }
    @Override
    public void deletePartS(Long scode) {
        sCodeRepository.deleteById(scode);
    }



    // 회사 분류 가져오기
    @Override
    public PartLDTO findPartL(Long lcode) {
        return partLTodto(lCodeRepository.getById(lcode));
    }
    @Override
    public PartMDTO findPartM(Long mcode) {
        return partMTOdto(mCodeRepository.getById(mcode));
    }
    @Override
    public PartSDTO findPartS(Long scode) {
        return partSTodto(sCodeRepository.getById(scode));
    }

    // 회사 분류 리스트 가져오기
    @Override
    public List<PartLDTO> findListPartL(Long type) {
        List<PartLDTO> list=new ArrayList<>();
        if(type==0L) {lCodeRepository.getListPartL1().forEach(x->list.add(partLTodto(x)));}
        else if(type==1L) {lCodeRepository.getListPartL2().forEach(x->list.add(partLTodto(x)));}
        else {lCodeRepository.findAll().forEach(x->list.add(partLTodto(x)));}
        return list;
    }
    @Override
    public List<PartMDTO> findListPartM(PartLDTO partLDTO, PartMDTO partMDTO, PartSDTO partSDTO, Long type) {
        List<PartMDTO> list=new ArrayList<>();
        Long temp=null;

        if(partSDTO!=null) temp=partSDTO.getPartMDTO().getPartLDTO().getPartLcode();
        else if(partMDTO!=null) temp=partMDTO.getPartLDTO().getPartLcode();
        else if(partLDTO!=null) temp=partLDTO.getPartLcode();

        if(temp==null){
            if(type==0L) mCodeRepository.getListPartM1().forEach(x->list.add(partMTOdto(x)));
            else if(type==1L) mCodeRepository.getListPartM2().forEach(x->list.add(partMTOdto(x)));
            else mCodeRepository.findAll().forEach(x->list.add(partMTOdto(x)));
        }else{
            mCodeRepository.getListPartMByPartL(temp).forEach(x->list.add(partMTOdto(x)));
        }

        return list;
    }
    @Override
    public List<PartSDTO> findListPartS(PartLDTO partLDTO, PartMDTO partMDTO, PartSDTO partSDTO, Long type) {
        List<PartSDTO> list=new ArrayList<>();
        Long temp=null;
        Long tempL=null;

        if(partLDTO!=null) tempL=partLDTO.getPartLcode();
        if(partMDTO!=null) temp=partMDTO.getPartMcode();
        if(partSDTO!=null) temp=partSDTO.getPartMDTO().getPartMcode();

        if(temp==null){
            if(tempL!=null) sCodeRepository.getListPartSByPartL(tempL).forEach(x->list.add(partSTodto(x)));
            else{
                if(type==0L) sCodeRepository.getListPartS1().forEach(x->list.add(partSTodto(x)));
                else if(type==1L) sCodeRepository.getListPartS2().forEach(x->list.add(partSTodto(x)));
                else sCodeRepository.findAll().forEach(x->list.add(partSTodto(x)));
            }
        }else{
            sCodeRepository.getListPartSByPartM(temp).forEach(x->list.add(partSTodto(x)));
        }
        return list;
    }
    @Override
    public PartCodeListDTO findListPartAll(PartLDTO partLDTO, PartMDTO partMDTO, PartSDTO partSDTO, Long type) {
        PartCodeListDTO list=PartCodeListDTO.builder()
                .partLDTOs(findListPartL(type))
                .partMDTOs(findListPartM(partLDTO, partMDTO, partSDTO, type))
                .partSDTOs(findListPartS(partLDTO, partMDTO, partSDTO, type))
                .build();
        return list;
    }

    // 회사분류 페이지 가져오기
    @Override
    public PageResultDTO<CategoryPartDTO, Object[]> getPagePart(Pageable pageable, Long code){
        if(code==0L){
            Page<Object[]> entityPage=lCodeRepository.getPagePartL(pageable);
            return new PageResultDTO<>(entityPage, this::exPartLToCategoryDTO);
        }else if(code==1L){
            Page<Object[]> entityPage=mCodeRepository.getPagePartM(pageable);
            return new PageResultDTO<>(entityPage, this::exPartMToCategoryDTO);
        }else{
            Page<Object[]> entityPage=sCodeRepository.getPagePartS(pageable);
            return new PageResultDTO<>(entityPage, this::exPartSToCategoryDTO);
        }
    }
    private CategoryPartDTO exPartLToCategoryDTO(Object[] objects){
        PartL partL=(PartL) objects[0];

        PartLDTO partLDTO=(partL!=null)? partLTodto(partL) : null;
        return new CategoryPartDTO(partLDTO, null, null);
    }
    private CategoryPartDTO exPartMToCategoryDTO(Object[] objects){
        PartM partM=(PartM) objects[0];
        PartL partL=(PartL) objects[1];

        PartMDTO partMDTO=(partM!=null)? partMTOdto(partM) : null;
        PartLDTO partLDTO=(partL!=null)? partLTodto(partL) : null;
        return new CategoryPartDTO(partLDTO, partMDTO, null);
    }
    private CategoryPartDTO exPartSToCategoryDTO(Object[] objects){
        PartS partS=(PartS) objects[0];
        PartM partM=(PartM) objects[1];
        PartL partL=(PartL) objects[2];

        PartSDTO partSDTO=(partS!=null)? partSTodto(partS) : null;
        PartMDTO partMDTO=(partM!=null)? partMTOdto(partM) : null;
        PartLDTO partLDTO=(partL!=null)? partLTodto(partL) : null;

        return new CategoryPartDTO(partLDTO, partMDTO, partSDTO);
    }
}
