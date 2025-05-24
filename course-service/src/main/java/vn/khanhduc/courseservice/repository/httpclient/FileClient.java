package vn.khanhduc.courseservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import vn.khanhduc.courseservice.configuration.AuthenticationRequestInterceptor;
import vn.khanhduc.courseservice.dto.response.FileResponse;
import vn.khanhduc.courseservice.dto.response.ResponseData;

@FeignClient(name = "FILE-SERVICE", configuration = AuthenticationRequestInterceptor.class)
public interface FileClient {

    @PostMapping(value = "/file/minio/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseData<FileResponse> uploadFileToMinio(@RequestPart("file")MultipartFile file,
                                                 @RequestPart("bucketName") String bucketName);

}
