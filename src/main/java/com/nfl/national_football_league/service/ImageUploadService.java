package com.nfl.national_football_league.service;

import com.nfl.national_football_league.constant.ImageUploadCategory;
import com.nfl.national_football_league.exception.FileUploadException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageUploadService {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public  class UploadImageInputParameter{
        private MultipartFile file;
        private String category;
        private String fileName;
    }
    public String uploadImage(UploadImageInputParameter uploadImageInputParameter) throws IOException, FileUploadException;
}
