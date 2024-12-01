package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.MaterialRepository;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageRequestDTO;
import mit.iwrcore.IWRCore.security.dto.PageDTO.PageResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final MemberService memberService;
    private final MaterService materService;
    private final BoxService boxService;

    // 저장, 삭제
    @Override
    public MaterialDTO saveMaterial(MaterialDTO dto, List<FileMaterial> fileList) {
        Material material = dtoToEntity(dto);
        material.setFiles(fileList);
        Material saved=materialRepository.save(material);
        return entityToDto(saved);
    }
    @Override
    public void deleteMaterial(Long materCode) {
        materialRepository.deleteById(materCode);
    }

    // 변환
    @Override
    public Material dtoToEntity(MaterialDTO dto) {
        Material entity = Material.builder()
                .materCode(dto.getMaterCode())
                .name(dto.getName())
                .unit(dto.getUnit())
                .standard(dto.getStandard())
                .color(dto.getColor())
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO()))
                .materS(materService.materSdtoToEntity(dto.getMaterSDTO()))
                .box(boxService.dtoToEntity(dto.getBoxDTO()))
                .build();
        return entity;
    }
    @Override
    public MaterialDTO entityToDto(Material entity) {
        MaterialDTO dto = MaterialDTO.builder()
                .materCode(entity.getMaterCode())
                .name(entity.getName())
                .unit(entity.getUnit())
                .standard(entity.getStandard())
                .color(entity.getColor())
                .memberDTO(memberService.memberTodto(entity.getWriter()))
                .materSDTO(materService.materSTodto(entity.getMaterS()))
                .boxDTO(boxService.entityToDto(entity.getBox()))
                .build();
        return dto;
    }

    // 조회
    @Override
    public MaterialDTO getMaterial(Long materCode) {
        Material material=materialRepository.findById(materCode).get();
        return entityToDto(material);
    }


    // 자재관리> 모든 자재 목록
    @Override
    public PageResultDTO<MaterialDTO, Material> getMaterialAll(PageRequestDTO requestDTO) {
        Page<Material> entityPage = materialRepository.findMaterialByCustomQuery(requestDTO);
        Function<Material, MaterialDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(entityPage, fn);
    }
    // 제품등록> 제품에 선택되지 않은 자재 목록
    @Override
    public PageResultDTO<MaterialDTO, Material> productMaterialList(PageRequestDTO requestDTO) {
        Page<Material> entityPage = materialRepository.findMaterialByCustomQuery2(requestDTO);
        Function<Material, MaterialDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(entityPage, fn);
    }
}



