package mit.iwrcore.IWRCore.controller_rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileMaterialDTO;
import mit.iwrcore.IWRCore.security.dto.FileDTO.FileProductDTO;
import mit.iwrcore.IWRCore.security.service.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/file")
@Log4j2
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/materialFile")
    public List<FileMaterialDTO> getMaterialFile(@RequestParam(required = false) Long code){
        return fileService.getMaterialFileList(code);
    }
    @GetMapping("/productFile")
    public List<FileProductDTO> getProductFile(@RequestParam(required = false) Long code){
        return fileService.getProductFileList(code);
    }

    @GetMapping("/thumbnail")
    @ResponseBody
    public ResponseEntity<byte[]> getThumbnail(String fileName, String path, String type, String name){
        String folder="";
        if(type.equals("m")) folder="material";
        else if(type.equals("p")) folder="product";

        File file=new File("C:\\iwlcore\\"+folder+"\\"+path+"\\"+fileName);

        ResponseEntity<byte[]> result=null;
        try{
            HttpHeaders header=new HttpHeaders();
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            result=new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
    @GetMapping("/readtxt")
    @ResponseBody
    public ResponseEntity<String> readtxt(String fileName, String path, String type, String name) {
        String folder = "";
        if (type.equals("m")) folder = "material";
        else if (type.equals("p")) folder = "product";

        File file = new File("C:\\iwlcore\\" + folder + "\\" + path + "\\" + fileName);

        ResponseEntity<String> result = null;
        try {
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type", "text/plain");
            result = new ResponseEntity<>(content, header, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> download(@RequestHeader("User-Agent") String userAgent, String fileName,
                                             String path, String type, String uuid){
        String folder="";
        if(type.equals("m")) folder="material";
        else if(type.equals("p")) folder="product";

        Resource resource=new FileSystemResource("C:\\iwlcore\\"+folder+"\\"+path+"\\"+fileName);

        if(resource.exists()==false){ return new ResponseEntity<>(HttpStatus.NOT_FOUND); }

        String resourceName=resource.getFilename().substring(uuid.length()+1);
        HttpHeaders headers=new HttpHeaders();

        try {
            String downloadName=null;
            if(userAgent.contains("Trident")){
                downloadName=URLEncoder.encode(resourceName, "UTF-8").replaceAll("\\+", " ");
            }else if(userAgent.contains("Edge")){
                downloadName=URLEncoder.encode(resourceName, "UTF-8");
            }else{
                downloadName=new String(resourceName.getBytes("UTF-8"), "ISO-8859-1");
            }
            headers.add("Content-Disposition", "attachment; filename="+downloadName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }

}
