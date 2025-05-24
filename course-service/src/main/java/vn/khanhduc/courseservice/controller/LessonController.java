package vn.khanhduc.courseservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.khanhduc.courseservice.dto.request.LessonCreationRequest;
import vn.khanhduc.courseservice.dto.response.LessonCreationResponse;
import vn.khanhduc.courseservice.dto.response.ResponseData;
import vn.khanhduc.courseservice.service.LessonService;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    ResponseData<LessonCreationResponse> creation(@RequestBody @Valid LessonCreationRequest request) {
        return ResponseData.<LessonCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .data(lessonService.create(request))
                .build();
    }

}
