package com.example.fileservice.service;

import com.example.fileservice.dto.FileMetadata;
import com.example.fileservice.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileResponse uploadFile(MultipartFile file);

    FileMetadata download(String fileName);
}
