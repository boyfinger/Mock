package com.vn.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileService {

    private static final String UPLOAD_FOLDER = "E:\\Documents\\pictures\\kekw\\";

    public String upload(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uniqueFileName = timeStamp + "_" + originalFileName;
        String destinationFile = UPLOAD_FOLDER + uniqueFileName;

        multipartFile.transferTo(new File(destinationFile));

        return "/images/" + uniqueFileName;
    }

}
