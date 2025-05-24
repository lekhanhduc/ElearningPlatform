package vn.khanhduc.courseservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.khanhduc.courseservice.dto.response.CourseCreationResponse;
import vn.khanhduc.courseservice.entity.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "courseId", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "categoryName", source = "category.name")
    CourseCreationResponse toCourseCreationResponse(Course course);

}
