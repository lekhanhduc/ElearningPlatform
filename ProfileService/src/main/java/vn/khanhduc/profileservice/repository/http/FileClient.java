package vn.khanhduc.profileservice.repository.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import vn.khanhduc.profileservice.configuration.AuthenticationRequestInterceptor;
import vn.khanhduc.profileservice.dto.response.FileResponse;
import vn.khanhduc.profileservice.dto.response.ResponseData;

@FeignClient(name = "FILE-SERVICE", configuration = {AuthenticationRequestInterceptor.class})
public interface FileClient {

    @PostMapping(value = "/file/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseData<FileResponse> uploadFile(@RequestPart("file") MultipartFile file);
}
