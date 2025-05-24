package vn.khanhduc.courseservice.service;

import org.springframework.web.multipart.MultipartFile;
import vn.khanhduc.courseservice.dto.request.CourseCreationRequest;
import vn.khanhduc.courseservice.dto.response.*;

public interface CourseService {
    CourseCreationResponse create(CourseCreationRequest request, MultipartFile file) throws Exception;
    CourseDetailResponse getAllDetailCourses(String courseId);
    PageResponse<CourseResponse> getAllCourses(int page, int size);
    PageResponse<CourseResponse> getAllCoursesByCategoryName(int page, int size, String categoryName);
    CourseExistsResponse existsById(String id);
}
