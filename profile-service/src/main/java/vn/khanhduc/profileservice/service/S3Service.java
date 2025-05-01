package vn.khanhduc.profileservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "S3-SERVICE")
public class S3Service {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.endpoint-url}")
    private String endpointUrl;

    private final S3Client s3Client;

    public String uploadFile(String folder, MultipartFile multipartFile) {
        File file = null;
        try {
            file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            uploadFileToS3(folder, fileName, file);
            return String.format("%s/%s/%s", endpointUrl, folder, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(file != null && file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    log.warn("Cannot remove file {}", file.getAbsolutePath());
                }
            }
        }
    }

    private void uploadFileToS3(String folder, String fileName, File file) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(folder + "/" + fileName)
                .contentType(MediaType.IMAGE_JPEG_VALUE)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        return file;
    }

    private String generateFileName(MultipartFile file) {
        return new Date().getTime() + "_" + file.getOriginalFilename();
    }
}
