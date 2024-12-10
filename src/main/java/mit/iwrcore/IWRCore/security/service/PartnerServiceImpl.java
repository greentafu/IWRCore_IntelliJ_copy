package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.PartnerRepository;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import mit.iwrcore.IWRCore.security.dto.PartnerDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService{
    private final PartnerRepository partnerRepository;
    private final PartCodeService partCodeService;

    // 추가, 삭제
    // 새로운 회사 계정 생성시 사업자등록번호 중복은 1, 아이디 중복은 2, 성공시에는 0
    @Override
    public Integer insertPartner(PartnerDTO dto) {
        if(dto.getId()!=null){
            Partner existPartnerID=findPartnerEntity(null, dto.getId(), null);
            Partner existPartnerReg=findPartnerEntity(null, null, dto.getRegistrationNumber());

            if(existPartnerReg==null){
                if(existPartnerID==null){
                    Partner partner=partnerDtoToEntity(dto);
                    partner.setPartnerRole(MemberRole.PARTNER);
                    partnerRepository.save(partner);
                    return 0;
                }else return 2;
            }else{
                if(dto.getPno()==null) return 1;
                else{
                    if(existPartnerReg.getPno().equals(dto.getPno())){
                        if(existPartnerID==null){
                            Partner partner=partnerDtoToEntity(dto);
                            partner.setPartnerRole(MemberRole.PARTNER);
                            partnerRepository.save(partner);
                            return 0;
                        }else{
                            if(existPartnerID.getPno().equals(dto.getPno())){
                                Partner partner=partnerDtoToEntity(dto);
                                partner.setPartnerRole(MemberRole.PARTNER);
                                partnerRepository.save(partner);
                                return 0;
                            }else return 2;
                        }
                    }else return 1;
                }
            }
        }else{
            Partner existPartnerReg=findPartnerEntity(null, null, dto.getRegistrationNumber());

            if(existPartnerReg==null){
                Partner partner=partnerDtoToEntity(dto);
                partner.setPartnerRole(MemberRole.PARTNER);
                partnerRepository.save(partner);
                return 0;
            }else{
                if(existPartnerReg.getPno().equals(dto.getPno())){
                    Partner partner=partnerDtoToEntity(dto);
                    partner.setPartnerRole(MemberRole.PARTNER);
                    partnerRepository.save(partner);
                    return 0;
                }else return 1;
            }
        }
    }
    @Override
    public void deletePartner(Long pno) {
        Partner partner=findPartnerEntity(pno, null, null);
        partnerRepository.delete(partner);
    }


    // 변환
    @Override
    public PartnerDTO partnerTodto(Partner entity){
        PartnerDTO dto=PartnerDTO.builder()
                .pno(entity.getPno())
                .name(entity.getName())
                .registrationNumber(entity.getRegistrationNumber())
                .location(entity.getLocation())
                .type(entity.getType())
                .sector(entity.getSector())
                .ceo(entity.getCeo())
                .telNumber(entity.getTelNumber())
                .email(entity.getEmail())
                .faxNumber(entity.getFaxNumber())
                .contacter(entity.getContacter())
                .contacterNumber(entity.getContacterNumber())
                .contacterEmail(entity.getContacterEmail())
                .roleSet(entity.getRoleSet())
                .id(entity.getId())
                .pw(entity.getPw())
                .password(entity.getPassword())
                .partSDTO(partCodeService.partSTodto(entity.getPartS()))
                .build();
        return dto;
    }
    @Override
    public Partner partnerDtoToEntity(PartnerDTO dto){
        Partner entity=Partner.builder()
                .pno(dto.getPno())
                .name(dto.getName())
                .registrationNumber(dto.getRegistrationNumber())
                .location(dto.getLocation())
                .type(dto.getType())
                .sector(dto.getSector())
                .ceo(dto.getCeo())
                .telNumber(dto.getTelNumber())
                .email(dto.getEmail())
                .faxNumber(dto.getFaxNumber())
                .contacter(dto.getContacter())
                .contacterNumber(dto.getContacterNumber())
                .contacterEmail(dto.getContacterEmail())
                .roleSet(dto.getRoleSet())
                .id(dto.getId())
                .pw(dto.getPw())
                .password(dto.getPassword())
                .partS(partCodeService.partSdtoToEntity(dto.getPartSDTO()))
                .build();
        return entity;
    }


    // 조회
    @Override
    public Partner findPartnerEntity(Long pno, String id, String reg_number) {
        return partnerRepository.findPartner(pno, id, reg_number);
    }
    @Override
    public PartnerDTO findPartnerDto(Long pno, String id, String reg_number) {
        Partner partner=partnerRepository.findPartner(pno, id, reg_number);
        return (partner!=null)?partnerTodto(partner):null;
    }
    @Override
    public List<PartnerDTO> partnerList(){
        List<Partner> entityList=partnerRepository.findAll();
        List<PartnerDTO> dtoList=(entityList!=null)?entityList.stream().map(this::partnerTodto).toList():null;
        return dtoList;
    }

    @Override
    public List<PartnerDTO> getPartnerByCategory(Long type, Long code){
        List<PartnerDTO> dtoList=new ArrayList<>();
        if(type==0){
            List<Partner> entityList=partnerRepository.getPartnerByCategoryL(code);
            if(entityList.size()!=0) entityList.forEach(x->dtoList.add(partnerTodto(x)));
        }else if(type==1){
            List<Partner> entityList=partnerRepository.getPartnerByCategoryM(code);
            if(entityList.size()!=0) entityList.forEach(x->dtoList.add(partnerTodto(x)));
        }else if(type==2){
            List<Partner> entityList=partnerRepository.getPartnerByCategoryS(code);
            if(entityList.size()!=0) entityList.forEach(x->dtoList.add(partnerTodto(x)));
        }
        return dtoList;
    }



    // 협력회사관리> 회사 모두 보기
    @Override
    public PageResultDTO<PartnerDTO, Partner> findPartnerList(PageRequestDTO requestDTO) {
        Page<Partner> partnerList=partnerRepository.findPartnerByCustomQuery(requestDTO);

        Function<Partner, PartnerDTO> fn=(entity->partnerTodto(entity));
        PageResultDTO pageResultDTO=new PageResultDTO<>(partnerList, fn);

        pageResultDTO.setPartL(requestDTO.getPartL());
        pageResultDTO.setPartM(requestDTO.getPartM());
        pageResultDTO.setPartS(requestDTO.getPartS());
        pageResultDTO.setPartnerSearch(requestDTO.getPartnerSearch());

        return pageResultDTO;
    }
    // 계약서> 소속회사 외 협력회사 모두 보기
    @Override
    public PageResultDTO<PartnerDTO, Partner> getAllPartner(PageRequestDTO requestDTO) {
        Page<Partner> partnerList=partnerRepository.getAllPartner(requestDTO);

        Function<Partner, PartnerDTO> fn=(entity->partnerTodto(entity));
        PageResultDTO pageResultDTO=new PageResultDTO<>(partnerList, fn);

        pageResultDTO.setPartL(requestDTO.getPartL());
        pageResultDTO.setPartM(requestDTO.getPartM());
        pageResultDTO.setPartS(requestDTO.getPartS());
        pageResultDTO.setPartnerSearch(requestDTO.getPartnerSearch());

        return pageResultDTO;
    }
    // 계약서> 소속회사 외 협력회사 모두 보기
    @Override
    public PageResultDTO<PartnerDTO, Partner> getBaljuPartner(PageRequestDTO requestDTO) {
        Page<Partner> partnerList=partnerRepository.getBaljuPartner(requestDTO);

        Function<Partner, PartnerDTO> fn=(entity->partnerTodto(entity));
        PageResultDTO pageResultDTO=new PageResultDTO<>(partnerList, fn);

        pageResultDTO.setPartL(requestDTO.getPartL());
        pageResultDTO.setPartM(requestDTO.getPartM());
        pageResultDTO.setPartS(requestDTO.getPartS());
        pageResultDTO.setPartnerSearch(requestDTO.getPartnerSearch());

        return pageResultDTO;
    }
    // 검수차수> 소속회사 외 검수차수 설정 가능한 협력회사 모두 보기
    @Override
    public PageResultDTO<PartnerDTO, Partner> getGumsuPartner(PageRequestDTO requestDTO) {
        Page<Partner> partnerList=partnerRepository.getGumsuPartner(requestDTO);

        Function<Partner, PartnerDTO> fn=(entity->partnerTodto(entity));
        PageResultDTO pageResultDTO=new PageResultDTO<>(partnerList, fn);

        pageResultDTO.setPartL(requestDTO.getPartL());
        pageResultDTO.setPartM(requestDTO.getPartM());
        pageResultDTO.setPartS(requestDTO.getPartS());
        pageResultDTO.setPartnerSearch(requestDTO.getPartnerSearch());

        return pageResultDTO;
    }
    // 거래명세서> 소속회사 외 거래명세서 발급가능한 협력회사 모두 보기
    @Override
    public PageResultDTO<PartnerDTO, Partner> getInvoicePartner(PageRequestDTO requestDTO) {
        Page<Partner> partnerList=partnerRepository.getInvoicePartner(requestDTO);

        Function<Partner, PartnerDTO> fn=(entity->partnerTodto(entity));
        PageResultDTO pageResultDTO=new PageResultDTO<>(partnerList, fn);

        pageResultDTO.setPartL(requestDTO.getPartL());
        pageResultDTO.setPartM(requestDTO.getPartM());
        pageResultDTO.setPartS(requestDTO.getPartS());
        pageResultDTO.setPartnerSearch(requestDTO.getPartnerSearch());

        return pageResultDTO;
    }
}
