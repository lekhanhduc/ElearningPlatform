package vn.khanhduc.courseservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.khanhduc.courseservice.dto.response.ChapterCreationResponse;
import vn.khanhduc.courseservice.entity.Chapter;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

    @Mapping(target = "chapterId", source = "id")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "userId", source = "course.userId")
    ChapterCreationResponse toChapterCreationResponse(Chapter chapter);
}
