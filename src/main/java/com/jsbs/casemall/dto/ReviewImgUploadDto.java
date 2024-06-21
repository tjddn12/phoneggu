package com.jsbs.casemall.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ReviewImgUploadDto {
    private List<MultipartFile> files;
}
