package vn.khanhduc.fileservice.repository;

import vn.khanhduc.fileservice.dto.FileInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Repository
public class FileRepository {

    @Value("${file.storage-dir}")
    private String storeDirectory;

    @Value("${file.download-prefix}")
    private String urlPrefix;

    public FileInformation saveToStorage(MultipartFile file) throws IOException {
        Path folder = Paths.get(storeDirectory);
        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        String fileName = filenameExtension != null
                ? UUID.randomUUID() + "." + filenameExtension
                : UUID.randomUUID().toString();

        Path filePath = folder.resolve(fileName).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return FileInformation.builder()
                .name(fileName)
                .contentType(file.getContentType())
                .size(file.getSize())
                .md5Checksum(DigestUtils.md5DigestAsHex(file.getInputStream()))
                .path(filePath.toString())
                .url(urlPrefix + fileName)
                .build();
    }

    public Resource read(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Path.of(path));
        return new ByteArrayResource(bytes);
    }

}
