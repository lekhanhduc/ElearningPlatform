package com.example.fileservice.service.impl;

import com.example.fileservice.dto.FileInformation;
import com.example.fileservice.dto.FileMetadata;
import com.example.fileservice.dto.response.FileResponse;
import com.example.fileservice.entity.FileManagement;
import com.example.fileservice.exception.ErrorCode;
import com.example.fileservice.exception.FileException;
import com.example.fileservice.repository.FileManagementRepository;
import com.example.fileservice.repository.FileRepository;
import com.example.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "FILE-SERVICE")
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileManagementRepository fileManagementRepository;

    @Override
    @PreAuthorize("isAuthenticated()")
    public FileResponse uploadFile(MultipartFile file) {
        Optional<String> principal = SecurityContextHolder.getContext().getAuthentication().getName()
                .describeConstable();

        if(principal.isEmpty()) {
            throw new FileException(ErrorCode.UNAUTHORIZED);
        }
        Long userId = Long.parseLong(principal.get());
        log.info("Uploading file");
        try {
            FileInformation fileInfo = fileRepository.saveToStorage(file);
            FileManagement fileManagement = FileManagement.builder()
                    .name(fileInfo.getName())
                    .contentType(fileInfo.getContentType())
                    .size(fileInfo.getSize())
                    .md5Checksum(fileInfo.getMd5Checksum())
                    .path(fileInfo.getPath())
                    .userId(userId)
                    .build();

            fileManagementRepository.save(fileManagement);
            log.info("Upload file successful");
            return FileResponse.builder()
                    .fileName(fileInfo.getName())
                    .fileType(fileInfo.getContentType())
                    .url(fileInfo.getUrl())
                    .build();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileMetadata download(String fileName) {
        FileManagement fileManagement = fileManagementRepository.findById(fileName)
                .orElseThrow(() -> new FileException(ErrorCode.FILE_NOT_FOUND));
        try {
            Resource resource = fileRepository.read(fileManagement.getPath());
            return FileMetadata.builder()
                    .contentType(fileManagement.getContentType())
                    .resource(resource)
                    .build();
        } catch (IOException e) {
            log.error("Error while download file {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
