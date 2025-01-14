package com.nfl.national_football_league.service.impl;

import com.nfl.national_football_league.constant.ImageUploadCategory;
import com.nfl.national_football_league.constant.RestErrorMessageDetail;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.domain.UploadedFileInfo;
import com.nfl.national_football_league.exception.FileUploadException;
import com.nfl.national_football_league.form.validation.EnumUtility;
import com.nfl.national_football_league.repository.UploadedFileInfoRepository;
import com.nfl.national_football_league.service.ImageUploadService;
import com.nfl.national_football_league.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Slf4j
@Service("ImageUploadService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageUploadServiceImpl implements ImageUploadService {
    @Value("${file.upload-dir}")
    private String rootDir;

    private final UploadedFileInfoRepository uploadedFileInfoRepository;

    @Override
    public String uploadImage(UploadImageInputParameter parameter) throws IOException, FileUploadException {
        if (parameter.getFile().isEmpty()) {
            throw new FileUploadException(
                    RestResponseMessage.FILE_UPLOAD_FAILURE,
                    RestResponseStatusCode.VALIDATION_ERROR_STATUS,
                    RestErrorMessageDetail.FILE_UPLOAD_FAILURE_ERROR_MESSAGE
            );
        }
        ImageUploadCategory category = EnumUtility.getEnumValue(ImageUploadCategory.class, parameter.getCategory());

        String subDirectory = parameter.getCategory();
        String uploadDir = rootDir + "\\" + subDirectory;
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileExtension = getFileExtension(Objects.requireNonNull(parameter.getFile().getOriginalFilename()));
        String newFileName = subDirectory + parameter.getFileName() + fileExtension;
        Path filePath = uploadPath.resolve(newFileName);

        Files.copy(parameter.getFile().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        UploadedFileInfo fileInfo = new UploadedFileInfo();
        fileInfo.setFileName(newFileName);
        fileInfo.setFileCategory(category);
        fileInfo.setFilePath(filePath.toString());
        fileInfo.setDeleteFlag(false);
        fileInfo.setCreatedDt(DateUtil.currentTime());
        fileInfo.setUpdatedDt(DateUtil.currentTime());
        uploadedFileInfoRepository.save(fileInfo);

        return filePath.toString();
    }

    private String getFileExtension(String originalFileName) {
        int lastIndex = originalFileName.lastIndexOf('.');
        return lastIndex != -1 ? originalFileName.substring(lastIndex) : "";
    }
}
