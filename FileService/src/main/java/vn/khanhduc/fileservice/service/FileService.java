package vn.khanhduc.fileservice.service;

import vn.khanhduc.fileservice.dto.FileMetadata;
import vn.khanhduc.fileservice.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileResponse uploadFile(MultipartFile file);
    FileMetadata download(String fileName);
}
