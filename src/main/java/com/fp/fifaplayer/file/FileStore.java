package com.fp.fifaplayer.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${seasonFile.dir}")
    private String seasonFileDir;
    @Value("${datanFile.dir}")
    private String datanFileDir;


    //파일이 여러개일때 - 시즌 이미지 파일
    public List<UploadFile> SeasonStoreFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileList.add(SeasonStoreFile(multipartFile));
            }
        }
        return storeFileList;
    }

    //파일이 하나일때 - 시즌 이미지 파일
    public UploadFile SeasonStoreFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getSeasonFullPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName);
    }

    //파일이 하나일때 - 데이터 이미지 파일
    public UploadFile DatanStoreFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getDatanFullPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName);
    }


    //경로 - 시즌 이미지
    public String getSeasonFullPath(String filename) {
        return seasonFileDir + filename;
    }

    //경로 - 데이터 이미지
    public String getDatanFullPath(String filename) {
        return datanFileDir + filename;
    }

    //UUID로 서버에 저장할 파일이름
    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExtension(originalFilename); //확장자 뽑기
        return uuid + "." + ext;
    }

    //확장자 뽑기
    private String extractExtension(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
