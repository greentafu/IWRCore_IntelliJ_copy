package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.File.FileMaterialRepository;
import mit.iwrcore.IWRCore.repository.File.FileProPlanRepository;
import mit.iwrcore.IWRCore.repository.File.FileProductRepository;
import mit.iwrcore.IWRCore.repository.MaterialRepository;
import mit.iwrcore.IWRCore.repository.ProPlanRepository;
import mit.iwrcore.IWRCore.repository.ProductRepository;
import mit.iwrcore.IWRCore.security.dto.FileDTO.AttachFileDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileMaterialDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileProPlanDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileProductDTO;
import mit.iwrcore.IWRCore.security.dto.MaterialDTO;
import mit.iwrcore.IWRCore.security.dto.ProductDTO;
import mit.iwrcore.IWRCore.security.dto.ProplanDTO;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final MaterialService materialService;
    private final MaterialRepository materialRepository;
    private final ProductService productService;
    private final ProplanService proplanService;
    private final MemberService memberService;

    private final ProductRepository productRepository;
    private final ProPlanRepository proPlanRepository;
    private final FileMaterialRepository fileMaterialRepository;
    private final FileProductRepository fileProductRepository;
    private final FileProPlanRepository fileProPlanRepository;

    // 파일 저장
    public void saveFile(Long code, AttachFileDTO attachFileDTO, String where){
        if(where.equals("m")){
            MaterialDTO materialDTO=materialService.findM(code);
            Material material=materialService.materdtoToEntity(materialDTO);
            FileMaterial fileMaterial=FileMaterial.builder()
                    .uuid(attachFileDTO.getUuid())
                    .fileName(attachFileDTO.getFileName())
                    .uploadPath(attachFileDTO.getUploadPath())
                    .contentType(attachFileDTO.getType())
                    .imgType((attachFileDTO.isImage())?1:0)
                    .material(material).build();
            fileMaterialRepository.save(fileMaterial);
        }else if(where.equals("p")){
            ProductDTO productDTO=productService.getProductById(code);
            Product product=productService.productDtoToEntity(productDTO);
            FileProduct fileProduct=FileProduct.builder()
                    .uuid(attachFileDTO.getUuid())
                    .fileName(attachFileDTO.getFileName())
                    .uploadPath(attachFileDTO.getUploadPath())
                    .contentType(attachFileDTO.getType())
                    .imgType((attachFileDTO.isImage())?1:0)
                    .product(product).build();
            fileProductRepository.save(fileProduct);
        }else if(where.equals("pp")){
            ProplanDTO proplanDTO=proplanService.findById(code);
            ProPlan proPlan=proplanService.dtoToEntity(proplanDTO);
            FileProPlan fileProPlan=FileProPlan.builder()
                    .uuid(attachFileDTO.getUuid())
                    .fileName(attachFileDTO.getFileName())
                    .uploadPath(attachFileDTO.getUploadPath())
                    .contentType(attachFileDTO.getType())
                    .imgType((attachFileDTO.isImage())?1:0)
                    .proPlan(proPlan).build();
            fileProPlanRepository.save(fileProPlan);
        }
    }
    // 파일 삭제
    public void deleteFile(String uuid, String where){
        if(where.equals("m")) {
            FileMaterial fileMaterial=fileMaterialRepository.findById(uuid).get();
            Material material=materialRepository.findById(fileMaterial.getMaterial().getMaterCode())
                    .orElseThrow(IllegalArgumentException::new);
            material.getFiles().remove(fileMaterial);
            fileMaterialRepository.deleteById(uuid);
        }else if(where.equals("p")) {
            FileProduct fileProduct=fileProductRepository.findById(uuid).get();
            Product product=productRepository.findById(fileProduct.getProduct().getManuCode())
                    .orElseThrow(IllegalArgumentException::new);
            product.getFiles().remove(fileProduct);
            fileProductRepository.deleteById(uuid);
        }else if(where.equals("pp")) {
            FileProPlan fileProPlan=fileProPlanRepository.findById(uuid).get();
            ProPlan proPlan=proPlanRepository.findById(fileProPlan.getProPlan().getProplanNo())
                    .orElseThrow(IllegalArgumentException::new);
            proPlan.getFiles().remove(fileProPlan);
            fileProPlanRepository.deleteById(uuid);
        }
    }

    // 파일 찾기
    public Object getFile(String uuid, String type){
        if(type.equals("m")){
            FileMaterial entity=fileMaterialRepository.findById(uuid).get();
            FileMaterialDTO dto=mFile_eTd(entity);
            return dto;
        }else if(type.equals("p")){
            FileProduct entity=fileProductRepository.findById(uuid).get();
            FileProductDTO dto=pFile_eTd(entity);
            return dto;
        }else if(type.equals("pp")){
            FileProPlan entity=fileProPlanRepository.findById(uuid).get();
            FileProPlanDTO dto=ppFile_eTd(entity);
            return dto;
        }
        return null;
    }

    // 자재코드에 따른 파일 목록
    public List<FileMaterialDTO> getMaterialFileList(Long materCode){
        List<FileMaterial> entityList=fileMaterialRepository.getMaterialFileListByMaterCode(materCode);
        List<FileMaterialDTO> dtoList=entityList.stream().map(x->mFile_eTd(x)).toList();
        return dtoList;
    }
    // 제품코드에 따른 파일 목록
    public List<FileProductDTO> getProductFileList(Long manuCode){
        List<FileProduct> entityList=fileProductRepository.getProductFileListByManuCode(manuCode);
        List<FileProductDTO> dtoList=entityList.stream().map(x->pFile_eTd(x)).toList();
        return dtoList;
    }
    // 생산계획에 따른 파일 목록
    public List<FileProPlanDTO> getProPlanFileList(Long proPlanNo){
        List<FileProPlan> entityList=fileProPlanRepository.getProPlanFileListByProPlanNo(proPlanNo);
        List<FileProPlanDTO> dtoList=entityList.stream().map(x->ppFile_eTd(x)).toList();
        return dtoList;
    }






    // 자재파일 entity-dto 변환
    public FileMaterial mFile_dTe(FileMaterialDTO dto){
        FileMaterial fileMaterial=FileMaterial.builder()
                .uuid(dto.getUuid()).fileName(dto.getFileName()).uploadPath(dto.getUploadPath())
                .contentType(dto.getContentType()).imgType(dto.getImgType())
                .material(materialService.materdtoToEntity(dto.getMaterialDTO())).build();
        return fileMaterial;
    }
    public FileMaterialDTO mFile_eTd(FileMaterial entity){
        FileMaterialDTO fileMaterialDTO= FileMaterialDTO.builder()
                .uuid(entity.getUuid()).fileName(entity.getFileName()).uploadPath(entity.getUploadPath())
                .contentType(entity.getContentType()).imgType(entity.getImgType())
                .materialDTO(materialService.materTodto(entity.getMaterial())).build();
        return fileMaterialDTO;
    }

    // 제품파일 entity-dto 변환
    public FileProduct pFile_dTe(FileProductDTO dto){
        FileProduct fileProduct=FileProduct.builder()
                .uuid(dto.getUuid()).fileName(dto.getFileName()).uploadPath(dto.getUploadPath())
                .contentType(dto.getContentType()).imgType(dto.getImgType())
                .product(productService.productDtoToEntity(dto.getProductDTO())).build();
        return fileProduct;
    }
    public FileProductDTO pFile_eTd(FileProduct entity){
        FileProductDTO fileProductDTO=FileProductDTO.builder()
                .uuid(entity.getUuid()).fileName(entity.getFileName()).uploadPath(entity.getUploadPath())
                .contentType(entity.getContentType()).imgType(entity.getImgType())
                .productDTO(productService.productEntityToDto(entity.getProduct())).build();
        return fileProductDTO;
    }

    // 생산계획 파일 entity-dto 변환
    public FileProPlan ppFile_dTe(FileProPlanDTO dto){
        FileProPlan fileProPlan=FileProPlan.builder()
                .uuid(dto.getUuid()).fileName(dto.getFileName()).uploadPath(dto.getUploadPath())
                .contentType(dto.getContentType()).imgType(dto.getImgType())
                .proPlan(proplanService.dtoToEntity(dto.getProplanDTO())).build();
        return fileProPlan;
    }
    public FileProPlanDTO ppFile_eTd(FileProPlan entity){
        FileProPlanDTO fileProPlanDTO=FileProPlanDTO.builder()
                .uuid(entity.getUuid()).fileName(entity.getFileName()).uploadPath(entity.getUploadPath())
                .contentType(entity.getContentType()).imgType(entity.getImgType())
                .proplanDTO(proplanService.entityToDTO(entity.getProPlan())).build();
        return fileProPlanDTO;
    }






    // 파일 처리 관련 service
    public void saveFileRun(MultipartFile[] files, Long code, String type){
        String folder="";
        if(type.equals("m")) folder="material";
        else if(type.equals("p")) folder="product";
        else if(type.equals("pp")) folder="proplan";

        // 파일 저장 위치
        String uploadFolder="C:\\iwlcore\\"+folder;
        String uploadFolderPath=getFolder();
        // 폴더 생성
        File uploadPath=new File(uploadFolder, uploadFolderPath);
        if(!uploadPath.exists()){ uploadPath.mkdirs(); }
        // 파일 저장
        for(MultipartFile multipartFile:files){
            String uploadFileName=multipartFile.getOriginalFilename();
            uploadFileName=uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
            UUID uuid=UUID.randomUUID();
            uploadFileName=uuid.toString()+"_"+uploadFileName;
            try {
                File saveFile=new File(uploadPath, uploadFileName);
                multipartFile.transferTo(saveFile);
                AttachFileDTO attachDTO=AttachFileDTO.builder()
                        .uuid(uuid.toString())
                        .fileName(uploadFileName)
                        .uploadPath(uploadFolderPath)
                        .type(multipartFile.getContentType())
                        .image(false).build();
                if(checkImageType(saveFile)){
                    attachDTO.setImage(true);
                    FileInputStream is=new FileInputStream((saveFile));
                    FileOutputStream thumbnail=new FileOutputStream(new File(uploadPath, "s_"+uploadFileName));
                    Thumbnailator.createThumbnail(is, thumbnail, 100, 100);
                    thumbnail.close();
                }
                saveFile(code, attachDTO, type);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private String getFolder(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String str=sdf.format(date);
        return str.replace("-", File.separator);
    }
    private boolean checkImageType(File file){
        try {
            String contentType= Files.probeContentType(file.toPath());
            return contentType.startsWith("image");
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public void deleteFileRun(List<String> deleteFile, String type){
        deleteFile.forEach(x-> {
            AttachFileDTO deFile=new AttachFileDTO();
            String folder="";
            if(type.equals("m")) {
                folder="material";
                Object object=getFile(x, "m");
                if(object!=null){
                    FileMaterialDTO dto=(FileMaterialDTO) object;
                    deFile.setFileName(dto.getFileName());
                    deFile.setUploadPath(dto.getUploadPath());
                    deFile.setImage(dto.getImgType()==1);
                }
            }else if(type.equals("p")){
                folder="product";
                Object object=getFile(x, "p");
                if(object!=null){
                    FileProductDTO dto=(FileProductDTO) object;
                    deFile.setFileName(dto.getFileName());
                    deFile.setUploadPath(dto.getUploadPath());
                    deFile.setImage(dto.getImgType()==1);
                }
            }else if(type.equals("pp")){
                folder="proplan";
                Object object=getFile(x, "pp");
                if(object!=null){
                    FileProPlanDTO dto=(FileProPlanDTO) object;
                    deFile.setFileName(dto.getFileName());
                    deFile.setUploadPath(dto.getUploadPath());
                    deFile.setImage(dto.getImgType()==1);
                }
            }
            if(deFile.getFileName()!=null){
                File file;
                String fileName=deFile.getFileName();
                String path=deFile.getUploadPath().replaceAll("\\\\", "/");
                try {
                    file=new File("C:\\iwlcore\\"+folder+"\\"+path+"\\"+ URLDecoder.decode(fileName, "UTF-8"));
                    file.delete();
                    if(deFile.isImage()){
                        fileName="s_"+fileName;
                        file=new File("C:\\iwlcore\\"+folder+"\\"+path+"\\"+ URLDecoder.decode(fileName, "UTF-8"));
                        file.delete();
                    }
                    deleteFile(x, type);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
