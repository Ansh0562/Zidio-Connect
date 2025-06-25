package com.zidioproject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String upload(MultipartFile file) throws IOException{
        Path path = Paths.get(uploadDir);

        // create direc if not exit
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }

        //Generate Unique File name
        String filename = UUID.randomUUID()+"_"+file.getOriginalFilename();

        //set target path
        Path targetpath = path.resolve(filename);

        //copy file to target path

        Files.copy(file.getInputStream(),targetpath, StandardCopyOption.REPLACE_EXISTING);

        // return  file path or URL
        return "/files/"+filename;
    }
}
