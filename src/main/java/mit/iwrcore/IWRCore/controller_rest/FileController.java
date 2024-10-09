package mit.iwrcore.IWRCore.controller_rest;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Log4j2
public class FileController {
    @PostMapping("upMaterial")
    public void upMaterial(MultipartFile[] uploadFiles){
        for(MultipartFile uploadFile:uploadFiles){
            String originalName=uploadFile.getOriginalFilename();
            String fileName=originalName.substring(originalName.lastIndexOf("\\")+1);
            log.info("fileName: "+fileName);
        }
    }
}
