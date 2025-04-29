package vn.khanhduc.fileservice.controller;

import vn.khanhduc.fileservice.dto.FileMetadata;
import vn.khanhduc.fileservice.dto.response.FileResponse;
import vn.khanhduc.fileservice.dto.response.ResponseData;
import vn.khanhduc.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseData<FileResponse> uploadFile(@RequestPart("file") MultipartFile file) {
        return ResponseData.<FileResponse>builder()
                .code(HttpStatus.OK.value())
                .data(fileService.uploadFile(file))
                .build();
    }

    @GetMapping("/download/{fileName}")
    ResponseEntity<Resource> uploadFile(@PathVariable String fileName) {
        FileMetadata fileMetadata = fileService.download(fileName);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, fileMetadata.getContentType());
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + fileMetadata.getContentType() +"\"");
        httpHeaders.add("Cache-Control", "public, max-age=300");

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(fileMetadata.getResource());
    }

}
