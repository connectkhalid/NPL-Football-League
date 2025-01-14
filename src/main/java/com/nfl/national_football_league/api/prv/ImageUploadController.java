package com.nfl.national_football_league.api.prv;

import com.nfl.national_football_league.constant.Constants;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.constant.ImageUploadCategory;
import com.nfl.national_football_league.exception.FileUploadException;
import com.nfl.national_football_league.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.nfl.national_football_league.constant.RestApiResponse.buildResponseWithDetails;

@Slf4j
@RestController
@CrossOrigin
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageUploadController {
    public static final String API_PATH_PLAYER_UPLOAD_PLAYER_IMAGE = Constants.ApiPath.PRIVATE_API_PATH + "/player/image";
    private final ImageUploadService imageUploadService;

    @PostMapping(path = API_PATH_PLAYER_UPLOAD_PLAYER_IMAGE,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> uploadPlayerImage(
            @RequestParam("image") MultipartFile image, @RequestParam("category") String category,
            @RequestParam("fileName") String fileName) throws IOException, FileUploadException {
        String filePath = imageUploadService.uploadImage(new ImageUploadService.UploadImageInputParameter(image, category, fileName));
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, filePath);
    }
}
