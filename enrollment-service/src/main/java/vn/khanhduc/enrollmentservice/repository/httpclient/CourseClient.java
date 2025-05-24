package vn.khanhduc.enrollmentservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.khanhduc.enrollmentservice.dto.response.CourseExistsResponse;
import vn.khanhduc.enrollmentservice.dto.response.ResponseData;

@FeignClient(name = "COURSE-SERVICE")
public interface CourseClient {

    @GetMapping(value = "/courses/{id}/exists")
    ResponseData<CourseExistsResponse> existsById(@PathVariable String id);
}
