package vn.khanhduc.fileservice.service.impl;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.khanhduc.fileservice.dto.response.FileResponse;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "MINIO-SERVICE")
public class MinioService {

    @Value("${minio.endpoint-url}")
    private String endpoint;

    private final MinioClient minioClient;

    public FileResponse uploadFileToMinio(MultipartFile file, String bucketName) throws Exception{
        String fileName = createFileName(file);
        if(!minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build())) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .contentType(file.getContentType())
                .stream(file.getInputStream(), file.getSize(), -1)
                .build());

        log.info("Upload file to Minio successfully. File name: {}, Bucket name: {}", fileName, bucketName);

        return FileResponse.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .url(String.format("%s/%s/%s", endpoint, bucketName, fileName))
                .build();
    }

    private String createFileName(MultipartFile file) {
        return System.currentTimeMillis() + "_" + file.getOriginalFilename();
    }
}
