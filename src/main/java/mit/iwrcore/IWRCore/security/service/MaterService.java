package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterCodeListDTO;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.MaterDTO.MaterSDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.CategoryMaterDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.CategoryProDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MaterService {
    // 회사 분류 삽입/수정
    MaterLDTO insertML(MaterLDTO dto);
    MaterMDTO insertMM(MaterMDTO dto);
    MaterSDTO insertMS(MaterSDTO dto);

    // 회사 분류 삭제
    void deleteMaterL(Long lcode);
    void deleteMaterM(Long mcode);
    void deleteMaterS(Long scode);


    // 회사 분류 가져오기
    MaterLDTO findMaterL(Long lcode);
    MaterMDTO findMaterM(Long mcode);
    MaterSDTO findMaterS(Long scode);
    // 회사 분류 리스트 가져오기
    List<MaterLDTO> findListMaterL(Long type);
    List<MaterMDTO> findListMaterM(MaterLDTO materLDTO, MaterMDTO materMDTO, MaterSDTO materSDTO, Long type);
    List<MaterSDTO> findListMaterS(MaterLDTO materLDTO, MaterMDTO materMDTO, MaterSDTO materSDTO, Long type);
    MaterCodeListDTO findListMaterAll(MaterLDTO materLDTO, MaterMDTO materMDTO, MaterSDTO materSDTO, Long type);


    PageResultDTO<CategoryMaterDTO, Object[]> getPageMater(Pageable pageable, Long code);


    // dto를 entity로
    default MaterL materLdtoToEntity(MaterLDTO dto){
        return MaterL.builder().materLcode(dto.getMaterLcode()).Lname(dto.getLname()).build();
    }
    default MaterM materMdtoToEntity(MaterMDTO dto){
        return MaterM.builder().materMcode(dto.getMaterMcode()).Mname(dto.getMname()).materL(materLdtoToEntity(dto.getMaterLDTO())).build();
    }
    default MaterS materSdtoToEntity(MaterSDTO dto){
        return MaterS.builder().materScode(dto.getMaterScode()).Sname(dto.getSname()).materM(materMdtoToEntity(dto.getMaterMDTO())).build();
    }

    // entity를 dto로
    default MaterLDTO materLTodto(MaterL entity){
        return MaterLDTO.builder().materLcode(entity.getMaterLcode()).lname(entity.getLname()).build();
    }
    default MaterMDTO materMTodto(MaterM entity){
        return MaterMDTO.builder().materMcode(entity.getMaterMcode()).Mname(entity.getMname()).materLDTO(materLTodto(entity.getMaterL())).build();
    }
    default MaterSDTO materSTodto(MaterS entity){
        return MaterSDTO.builder().materScode(entity.getMaterScode()).Sname(entity.getSname()).materMDTO(materMTodto(entity.getMaterM())).build();
    }
}