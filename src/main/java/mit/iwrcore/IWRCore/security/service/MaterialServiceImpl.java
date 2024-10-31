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

    @Override
    public MaterialDTO saveMaterial(MaterialDTO dto, List<FileMaterial> fileList) {
        Material material = materdtoToEntity(dto);
        material.setFiles(fileList);
        Material saved=materialRepository.save(material);
        return materTodto(saved);
    }
    @Override
    public void deleteMaterial(Long materCode) {
        log.info("Deleting material with code: {}", materCode);
        materialRepository.deleteById(materCode);
    }
    @Override
    public MaterialDTO findM(Long materCode) {
        log.info("Finding material with code: {}", materCode);
        return materialRepository.findById(materCode)
                .map(this::materTodto)
                .orElseThrow(() -> new RuntimeException("Material not found"));
    }

    @Override
    public PageResultDTO<MaterialDTO, Material> findMaterialAll(PageRequestDTO requestDTO) {
        Page<Material> entityPage = materialRepository.findMaterialByCustomQuery(requestDTO);
        Function<Material, MaterialDTO> fn = (entity -> materTodto(entity));

        PageResultDTO pageResultDTO = new PageResultDTO<>(entityPage, fn);

        pageResultDTO.setMaterL(requestDTO.getMaterL());
        pageResultDTO.setMaterM(requestDTO.getMaterM());
        pageResultDTO.setMaterS(requestDTO.getMaterS());
        pageResultDTO.setMaterialSearch(requestDTO.getMaterialSearch());
        pageResultDTO.setBox(requestDTO.getBox());
        pageResultDTO.setProL(requestDTO.getProL());
        pageResultDTO.setProM(requestDTO.getProM());
        pageResultDTO.setProS(requestDTO.getProS());
        pageResultDTO.setProductSearch(requestDTO.getProductSearch());

        return pageResultDTO;
    }
    @Override
    public PageResultDTO<MaterialDTO, Material> productMaterialList(PageRequestDTO requestDTO) {
        Page<Material> entityPage = materialRepository.findMaterialByCustomQuery2(requestDTO);
        Function<Material, MaterialDTO> fn = (entity -> materTodto(entity));
        return new PageResultDTO<>(entityPage, fn);
    }

    @Override
    public List<Material> findMaterialPart(Long boxcode, Long materscode) {
        return materialRepository.materialListPart(boxcode, materscode);
    }

    @Override
    public List<MaterialDTO> materialList() {
        return materialRepository.findAll().stream().map(this::materTodto).toList();
    }



    // dto를 entity로
    @Override
    public Material materdtoToEntity(MaterialDTO dto) {
        Material entity = Material.builder()
                .materCode(dto.getMaterCode())
                .name(dto.getName())
                .unit(dto.getUnit())
                .standard(dto.getStandard())
                .color(dto.getColor())
                .file(dto.getFile())
                .writer(memberService.memberdtoToEntity(dto.getMemberDTO()))
                .materS(materService.materSdtoToEntity(dto.getMaterSDTO()))
                .box(boxService.boxdtoToEntity(dto.getBoxDTO()))
                .build();
        return entity;
    }

    // entity를 dto로
    @Override
    public MaterialDTO materTodto(Material entity) {
        MaterialDTO dto = MaterialDTO.builder()
                .materCode(entity.getMaterCode())
                .name(entity.getName())
                .unit(entity.getUnit())
                .standard(entity.getStandard())
                .color(entity.getColor())
                .file(entity.getFile())
                .memberDTO(memberService.memberTodto(entity.getWriter()))
                .materSDTO(materService.materSTodto(entity.getMaterS()))
                .boxDTO(boxService.boxTodto(entity.getBox()))
                .build();
        return dto;
    }

}



