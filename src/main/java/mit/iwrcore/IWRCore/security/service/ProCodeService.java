package mit.iwrcore.IWRCore.security.service;

import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProCodeListDTO;
import mit.iwrcore.IWRCore.entity.ProL;
import mit.iwrcore.IWRCore.entity.ProM;
import mit.iwrcore.IWRCore.entity.ProS;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProLDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProMDTO;
import mit.iwrcore.IWRCore.security.dto.CategoryDTO.ProDTO.ProSDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.CategoryPartDTO;
import mit.iwrcore.IWRCore.security.dto.multiDTO.CategoryProDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProCodeService {

    // 회사 분류 삽입
    ProLDTO insertProL(ProLDTO dto);
    ProMDTO insertProM(ProMDTO dto);
    ProSDTO insertProS(ProSDTO dto);

    // 회사 분류 삭제
    void deleteProL(Long lcode);
    void deleteProM(Long mcode);
    void deleteProS(Long scode);



    // 회사 분류 가져오기
    ProLDTO findProL(Long lcode);
    ProMDTO findProM(Long mcode);
    ProSDTO findProS(Long scode);

    // 회사 분류 리스트 가져오기
    List<ProLDTO> findListProL(Long code);
    List<ProMDTO> findListProM(ProLDTO proLDTO, ProMDTO proMDTO, ProSDTO proSDTO, Long code);
    List<ProSDTO> findListProS(ProLDTO proLDTO, ProMDTO proMDTO, ProSDTO proSDTO, Long code);
    ProCodeListDTO findListProAll(ProLDTO proLDTO, ProMDTO proMDTO, ProSDTO proSDTO, Long code);

    PageResultDTO<CategoryProDTO, Object[]> getPagePro(Pageable pageable, Long code);



    // dto를 entity로
    default ProL proLdtoToEntity(ProLDTO dto){
        return ProL.builder().proLcode(dto.getProLcode()).Lname(dto.getLname()).build();
    }
    default ProM proMdtoToEntity(ProMDTO dto){
        return ProM.builder().proMcode(dto.getProMcode()).Mname(dto.getMname()).proL(proLdtoToEntity(dto.getProLDTO())).build();
    }
    default ProS proSdtoToEntity(ProSDTO dto){
        return ProS.builder().proScode(dto.getProScode()).Sname(dto.getSname()).proM(proMdtoToEntity(dto.getProMDTO())).build();
    }

    // entity를 dto로
    default ProLDTO proLTodto(ProL entity){
        return ProLDTO.builder().proLcode(entity.getProLcode()).Lname(entity.getLname()).build();
    }
    default ProMDTO

    proMTodto(ProM entity){
        return ProMDTO.builder().proMcode(entity.getProMcode()).Mname(entity.getMname()).proLDTO(proLTodto(entity.getProL())).build();
    }
    default ProSDTO proSTodto(ProS entity){
        return ProSDTO.builder().proScode(entity.getProScode()).Sname(entity.getSname()).proMDTO(proMTodto(entity.getProM())).build();
    }
}
