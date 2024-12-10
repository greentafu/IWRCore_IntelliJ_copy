package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterCodeListDTO;
import mit.iwrcore.IWRCore.repository.Category.Mater.MaterLRepository;
import mit.iwrcore.IWRCore.repository.Category.Mater.MaterMRepository;
import mit.iwrcore.IWRCore.repository.Category.Mater.MaterSRepository;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterSDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProSDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.CategoryMaterDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.CategoryProDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class MaterServiceImpl implements MaterService{
    private final MaterLRepository materLRepository;
    private final MaterMRepository materMRepository;
    private final MaterSRepository materSRepository;

    @Override
    public MaterLDTO insertML(MaterLDTO dto){
        MaterL materL=materLdtoToEntity(dto);
        MaterL savedL=materLRepository.save(materL);
        return materLTodto(savedL);
    }
    @Override
    public MaterMDTO insertMM(MaterMDTO dto){
        MaterM materM=materMdtoToEntity(dto);
        MaterM saved=materMRepository.save(materM);
        return materMTodto(saved);
    }
    @Override
    public MaterSDTO insertMS(MaterSDTO dto){
        MaterS materS=materSdtoToEntity(dto);
        MaterS saved=materSRepository.save(materS);
        return materSTodto(saved);
    }
    @Override
    public void deleteMaterL(Long lcode){materLRepository.deleteById(lcode);}
    @Override
    public void deleteMaterM(Long mcode){materMRepository.deleteById(mcode);}
    @Override
    public void deleteMaterS(Long scode){materSRepository.deleteById(scode);}

    @Override
    public MaterLDTO findMaterL(Long lcode){return materLTodto(materLRepository.getById(lcode));}
    @Override
    public MaterMDTO findMaterM(Long mcode){return materMTodto(materMRepository.getById(mcode));}
    @Override
    public MaterSDTO findMaterS(Long scode){return materSTodto(materSRepository.getById(scode));}

    @Override
    public List<MaterLDTO> findListMaterL(Long type) {
        List<MaterLDTO> list=new ArrayList<>();
        if(type==0L) materLRepository.findAll().forEach(x->list.add(materLTodto(x)));
        else materLRepository.getListMaterL1().forEach(x->list.add(materLTodto(x)));
        return list;
    }
    @Override
    public List<MaterMDTO> findListMaterM(MaterLDTO materLDTO, MaterMDTO materMDTO, MaterSDTO materSDTO, Long type) {
        List<MaterMDTO> list=new ArrayList<>();
        Long temp=null;

        if(materSDTO!=null) temp=materSDTO.getMaterMDTO().getMaterLDTO().getMaterLcode();
        else if(materMDTO!=null) temp=materMDTO.getMaterLDTO().getMaterLcode();
        else if(materLDTO!=null) temp=materLDTO.getMaterLcode();

        if(temp==null){
            if(type==0L) materMRepository.findAll().forEach(x->list.add(materMTodto(x)));
            else materMRepository.getListMaterM1().forEach(x->list.add(materMTodto(x)));
        }else{
            materMRepository.getListMaterMByMaterL(temp).forEach(x->list.add(materMTodto(x)));
        }

        return list;
    }
    @Override
    public List<MaterSDTO> findListMaterS(MaterLDTO materLDTO, MaterMDTO materMDTO, MaterSDTO materSDTO, Long type) {
        List<MaterSDTO> list=new ArrayList<>();
        Long temp=null;
        Long tempL=null;

        if(materLDTO!=null) tempL=materLDTO.getMaterLcode();
        if(materMDTO!=null) temp=materMDTO.getMaterMcode();
        if(materSDTO!=null) temp=materSDTO.getMaterMDTO().getMaterMcode();

        if(temp==null){
            if(tempL!=null) materSRepository.getListMaterByMaterL(tempL).forEach(x->list.add(materSTodto(x)));
            else{
                if(type==0L) materSRepository.findAll().forEach(x->list.add(materSTodto(x)));
                else materSRepository.getListMaterS1().forEach(x->list.add(materSTodto(x)));
            }
        }else{
            materSRepository.getListMaterSByMaterM(temp).forEach(x->list.add(materSTodto(x)));
        }
        return list;
    }
    @Override
    public MaterCodeListDTO findListMaterAll(MaterLDTO materLDTO, MaterMDTO materMDTO, MaterSDTO materSDTO, Long type) {
        MaterCodeListDTO list=MaterCodeListDTO.builder()
                .materLDTOs(findListMaterL(type))
                .materMDTOs(findListMaterM(materLDTO, materMDTO, materSDTO, type))
                .materSDTOs(findListMaterS(materLDTO, materMDTO, materSDTO, type))
                .build();
        return list;
    }


    // 자재분류 페이지 가져오기
    @Override
    public PageResultDTO<CategoryMaterDTO, Object[]> getPageMater(Pageable pageable, Long code){
        if(code==0L){
            Page<Object[]> entityPage=materLRepository.getPageMaterL(pageable);
            return new PageResultDTO<>(entityPage, this::exMaterLToCategoryDTO);
        }else if(code==1L){
            Page<Object[]> entityPage=materMRepository.getPageMaterM(pageable);
            return new PageResultDTO<>(entityPage, this::exMaterMToCategoryDTO);
        }else{
            Page<Object[]> entityPage=materSRepository.getPageMaterS(pageable);
            return new PageResultDTO<>(entityPage, this::exMaterSToCategoryDTO);
        }
    }
    private CategoryMaterDTO exMaterLToCategoryDTO(Object[] objects){
        MaterL materL=(MaterL) objects[0];

        MaterLDTO materLDTO=(materL!=null)? materLTodto(materL) : null;
        return new CategoryMaterDTO(materLDTO, null, null);
    }
    private CategoryMaterDTO exMaterMToCategoryDTO(Object[] objects){
        MaterM materM=(MaterM) objects[0];
        MaterL materL=(MaterL) objects[1];

        MaterMDTO materMDTO=(materM!=null)? materMTodto(materM) : null;
        MaterLDTO materLDTO=(materL!=null)? materLTodto(materL) : null;
        return new CategoryMaterDTO(materLDTO, materMDTO, null);
    }
    private CategoryMaterDTO exMaterSToCategoryDTO(Object[] objects){
        MaterS materS=(MaterS) objects[0];
        MaterM materM=(MaterM) objects[1];
        MaterL materL=(MaterL) objects[2];

        MaterSDTO materSDTO=(materS!=null)? materSTodto(materS) : null;
        MaterMDTO materMDTO=(materM!=null)? materMTodto(materM) : null;
        MaterLDTO materLDTO=(materL!=null)? materLTodto(materL) : null;
        return new CategoryMaterDTO(materLDTO, materMDTO, materSDTO);
    }


}