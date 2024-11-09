package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.*;
import mit.iwrcore.IWRCore.repository.File.*;
import mit.iwrcore.IWRCore.security.dto.*;
import mit.iwrcore.IWRCore.security.dto.FileDTO.*;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private final ContractService contractService;
    private final ReturnsService returnsService;

    private final ProductRepository productRepository;
    private final ProPlanRepository proPlanRepository;
    private final ContractRepository contractRepository;
    private final ReturnsRepository returnsRepository;

    private final FileMaterialRepository fileMaterialRepository;
    private final FileProductRepository fileProductRepository;
    private final FileProPlanRepository fileProPlanRepository;
    private final FileContractRepository fileContractRepository;
    private final FileReturnsRepository fileReturnsRepository;

    // 파일 저장
    public void saveFile(Long code, AttachFileDTO attachFileDTO, String where){
        if(where.equals("m")){
            MaterialDTO materialDTO=materialService.getMaterial(code);
            Material material=materialService.dtoToEntity(materialDTO);
            FileMaterial fileMaterial=FileMaterial.builder()
                    .uuid(attachFileDTO.getUuid())
                    .fileName(attachFileDTO.getFileName())
                    .uploadPath(attachFileDTO.getUploadPath())
                    .contentType(attachFileDTO.getType())
                    .imgType((attachFileDTO.isImage())?1:0)
                    .material(material).build();
            fileMaterialRepository.save(fileMaterial);
        }else if(where.equals("p")){
            ProductDTO productDTO=productService.getProduct(code);
            Product product=productService.dtoToEntity(productDTO);
            FileProduct fileProduct=FileProduct.builder()
                    .uuid(attachFileDTO.getUuid())
                    .fileName(attachFileDTO.getFileName())
                    .uploadPath(attachFileDTO.getUploadPath())
                    .contentType(attachFileDTO.getType())
                    .imgType((attachFileDTO.isImage())?1:0)
                    .product(product).build();
            fileProductRepository.save(fileProduct);
        }else if(where.equals("pp")){
            ProplanDTO proplanDTO=proplanService.getProPlan(code);
            ProPlan proPlan=proplanService.dtoToEntity(proplanDTO);
            FileProPlan fileProPlan=FileProPlan.builder()
                    .uuid(attachFileDTO.getUuid())
                    .fileName(attachFileDTO.getFileName())
                    .uploadPath(attachFileDTO.getUploadPath())
                    .contentType(attachFileDTO.getType())
                    .imgType((attachFileDTO.isImage())?1:0)
                    .proPlan(proPlan).build();
            fileProPlanRepository.save(fileProPlan);
        }else if(where.equals("c")){
            ContractDTO contractDTO=contractService.getContract(code);
            Contract contract=contractService.dtoToEntity(contractDTO);
            FileContract fileContract=FileContract.builder()
                    .uuid(attachFileDTO.getUuid())
                    .fileName(attachFileDTO.getFileName())
                    .uploadPath(attachFileDTO.getUploadPath())
                    .contentType(attachFileDTO.getType())
                    .imgType((attachFileDTO.isImage())?1:0)
                    .contract(contract).build();
            fileContractRepository.save(fileContract);
        }else if(where.equals("r")){
            ReturnsDTO returnsDTO=returnsService.getReturns(code);
            Returns returns=returnsService.dtoToEntity(returnsDTO);
            FileReturns fileReturns=FileReturns.builder()
                    .uuid(attachFileDTO.getUuid())
                    .fileName(attachFileDTO.getFileName())
                    .uploadPath(attachFileDTO.getUploadPath())
                    .contentType(attachFileDTO.getType())
                    .imgType((attachFileDTO.isImage())?1:0)
                    .returns(returns).build();
            fileReturnsRepository.save(fileReturns);
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
        }else if(where.equals("c")){
            FileContract fileContract=fileContractRepository.findById(uuid).get();
            Contract contract=contractRepository.findById(fileContract.getContract().getConNo())
                    .orElseThrow(IllegalArgumentException::new);
            contract.getFiles().remove(fileContract);
            fileContractRepository.deleteById(uuid);
        }else if(where.equals("r")){
            FileReturns fileReturns=fileReturnsRepository.findById(uuid).get();
            Returns returns=returnsRepository.findById(fileReturns.getReturns().getReNO())
                    .orElseThrow(IllegalArgumentException::new);
            returns.getFiles().remove(fileReturns);
            fileReturnsRepository.deleteById(uuid);
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
        }else if(type.equals("c")){
            FileContract entity=fileContractRepository.findById(uuid).get();
            FileContractDTO dto=cFile_eTd(entity);
            return dto;
        }else if(type.equals("r")){
            FileReturns entity=fileReturnsRepository.findById(uuid).get();
            FileReturnsDTO dto=rFile_eTd(entity);
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
    // 계약서에 따른 파일 목록
    public List<FileContractDTO> getContractFileList(Long conNo){
        List<FileContract> entityList=fileContractRepository.getContractFileListByConNo(conNo);
        List<FileContractDTO> dtoList=entityList.stream().map(x->cFile_eTd(x)).toList();
        return dtoList;
    }
    // 반품에 따른 파일 목록
    public List<FileReturnsDTO> getReturnsFileList(Long reNO){
        List<FileReturns> entityList=fileReturnsRepository.getReturnsFileListByReNO(reNO);
        List<FileReturnsDTO> dtoList=entityList.stream().map(x->rFile_eTd(x)).toList();
        return dtoList;
    }




    // multipartFile->AttachFileDTO
    public List<AttachFileDTO> fileCopy(MultipartFile[] files, String type, int count){
        List<AttachFileDTO> result = new ArrayList<>();

        String folder = "";
        if (type.equals("m")) folder = "material";
        else if (type.equals("p")) folder = "product";
        else if (type.equals("pp")) folder = "proplan";
        else if (type.equals("c")) folder = "contract";
        else if (type.equals("r")) folder = "returns";

        // 파일 저장 위치
        String uploadFolder = "C:\\iwlcore\\" + folder;
        String uploadFolderPath = getFolder();
        // 폴더 생성
        File uploadPath = new File(uploadFolder, uploadFolderPath);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        // 파일 저장
        for (MultipartFile multipartFile : files) {
            String uploadFileName = multipartFile.getOriginalFilename();
            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
            try {
                for (int i = 0; i < count; i++) {
                    UUID uuid = UUID.randomUUID();
                    String saveuploadFileName = uuid.toString() + "_" + uploadFileName;

                    File saveFile = new File(uploadPath, saveuploadFileName);

                    // 파일 저장
                    try (InputStream is = multipartFile.getInputStream();
                         FileOutputStream fos = new FileOutputStream(saveFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }

                    if (!saveFile.exists()) { continue; }

                    AttachFileDTO attachDTO = AttachFileDTO.builder()
                            .uuid(uuid.toString())
                            .fileName(saveuploadFileName)
                            .uploadPath(uploadFolderPath)
                            .type(multipartFile.getContentType())
                            .image(false).build();

                    if (checkImageType(saveFile)) {
                        attachDTO.setImage(true);
                        try (FileInputStream is = new FileInputStream(saveFile);
                             FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + saveuploadFileName))) {
                            Thumbnailator.createThumbnail(is, thumbnail, 100, 100);
                        }
                    }

                    result.add(attachDTO);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }





    // 자재파일 entity-dto 변환
    public FileMaterial mFile_dTe(FileMaterialDTO dto){
        FileMaterial fileMaterial=FileMaterial.builder()
                .uuid(dto.getUuid()).fileName(dto.getFileName()).uploadPath(dto.getUploadPath())
                .contentType(dto.getContentType()).imgType(dto.getImgType())
                .material(materialService.dtoToEntity(dto.getMaterialDTO())).build();
        return fileMaterial;
    }
    public FileMaterialDTO mFile_eTd(FileMaterial entity){
        FileMaterialDTO fileMaterialDTO= FileMaterialDTO.builder()
                .uuid(entity.getUuid()).fileName(entity.getFileName()).uploadPath(entity.getUploadPath())
                .contentType(entity.getContentType()).imgType(entity.getImgType())
                .materialDTO(materialService.entityToDto(entity.getMaterial())).build();
        return fileMaterialDTO;
    }

    // 제품파일 entity-dto 변환
    public FileProduct pFile_dTe(FileProductDTO dto){
        FileProduct fileProduct=FileProduct.builder()
                .uuid(dto.getUuid()).fileName(dto.getFileName()).uploadPath(dto.getUploadPath())
                .contentType(dto.getContentType()).imgType(dto.getImgType())
                .product(productService.dtoToEntity(dto.getProductDTO())).build();
        return fileProduct;
    }
    public FileProductDTO pFile_eTd(FileProduct entity){
        FileProductDTO fileProductDTO=FileProductDTO.builder()
                .uuid(entity.getUuid()).fileName(entity.getFileName()).uploadPath(entity.getUploadPath())
                .contentType(entity.getContentType()).imgType(entity.getImgType())
                .productDTO(productService.entityToDto(entity.getProduct())).build();
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
    // 계약서 파일 entity-dto변환
    public FileContract cFile_dTe(FileContractDTO dto){
        FileContract fileContract=FileContract.builder()
                .uuid(dto.getUuid()).fileName(dto.getFileName()).uploadPath(dto.getUploadPath())
                .contentType(dto.getContentType()).imgType(dto.getImgType())
                .contract(contractService.dtoToEntity(dto.getContractDTO())).build();
        return fileContract;
    }
    public FileContractDTO cFile_eTd(FileContract entity){
        FileContractDTO fileContractDTO=FileContractDTO.builder()
                .uuid(entity.getUuid()).fileName(entity.getFileName()).uploadPath(entity.getUploadPath())
                .contentType(entity.getContentType()).imgType(entity.getImgType())
                .contractDTO(contractService.entityToDTO(entity.getContract())).build();
        return fileContractDTO;
    }
    // 반품 파일 entity-dto변환
    public FileReturns rFile_dTe(FileReturnsDTO dto){
        FileReturns fileReturns=FileReturns.builder()
                .uuid(dto.getUuid()).fileName(dto.getFileName()).uploadPath(dto.getUploadPath())
                .contentType(dto.getContentType()).imgType(dto.getImgType())
                .returns(returnsService.dtoToEntity(dto.getReturnsDTO())).build();
        return fileReturns;
    }
    public FileReturnsDTO rFile_eTd(FileReturns entity){
        FileReturnsDTO fileReturnsDTO=FileReturnsDTO.builder()
                .uuid(entity.getUuid()).fileName(entity.getFileName()).uploadPath(entity.getUploadPath())
                .contentType(entity.getContentType()).imgType(entity.getImgType())
                .returnsDTO(returnsService.entityToDTO(entity.getReturns())).build();
        return fileReturnsDTO;
    }





    // 파일 처리 관련 service
    public void saveFileRun(MultipartFile[] files, Long code, String type){
        String folder="";
        if(type.equals("m")) folder="material";
        else if(type.equals("p")) folder="product";
        else if(type.equals("pp")) folder="proplan";
        else if(type.equals("c")) folder="contract";
        else if (type.equals("r")) folder = "returns";

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
            }else if(type.equals("c")){
                folder="contract";
                Object object=getFile(x, "c");
                if(object!=null){
                    FileContractDTO dto=(FileContractDTO) object;
                    deFile.setFileName(dto.getFileName());
                    deFile.setUploadPath(dto.getUploadPath());
                    deFile.setImage(dto.getImgType()==1);
                }
            }else if(type.equals("r")){
                folder="returns";
                Object object=getFile(x, "r");
                if(object!=null){
                    FileReturnsDTO dto=(FileReturnsDTO) object;
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
