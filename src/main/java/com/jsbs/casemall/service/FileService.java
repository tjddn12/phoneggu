package com.jsbs.casemall.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath,String originalFileName, byte[] fileData) throws Exception{
        Path path = Paths.get(uploadPath).toAbsolutePath().normalize(); // 상대 경로를 절대 경로로 변환
        if (!Files.exists(path)) {
            Files.createDirectories(path); // 디렉토리가 없으면 생성
        }
        UUID uuid = UUID.randomUUID(); //파일 이름 생성
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        //lastIndexOf() 메서드는 문자열에서 지정된 문자또는  문자열이 마지막으로 등장하는 위치 인덱스 반환
        //substring(4) 4부터 끝까지   abc.jpg -> jpg
        String savedFileName =  uuid.toString() + extension;
        //암호화된 uuid에 확장명 붙여서 새로운 파일명을 제작
        String fileUploadFullUrl= uploadPath + "/" + savedFileName; //경로를 포함한 저장이름
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData); //파일 쓰기
        fos.close();

        return savedFileName; //업로드된 파일 이름 반환
    }

    public String reviewUploadFile(String reviewUploadPath,String originalFileName, byte[] fileData) throws Exception{
        Path path = Paths.get(reviewUploadPath).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            Files.createDirectories(path); // 디렉토리가 없으면 생성
        }
        UUID uuid = UUID.randomUUID(); //파일 이름 생성
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        //lastIndexOf() 메서드는 문자열에서 지정된 문자또는  문자열이 마지막으로 등장하는 위치 인덱스 반환
        //substring(4) 4부터 끝까지   abc.jpg -> jpg
        String savedFileName =  uuid.toString() + extension;
        //암호화된 uuid에 확장명 붙여서 새로운 파일명을 제작
        String fileUploadFullUrl= reviewUploadPath + "/" + savedFileName; //경로를 포함한 저장이름
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData); //파일 쓰기
        fos.close();

        return savedFileName; //업로드된 파일 이름 반환
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath); //파일이 저장된 경로로 파일 객체 생성
        if(deleteFile.exists()) { //파일이 존재하면 파일 삭제
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
