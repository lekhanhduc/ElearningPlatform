package vn.khanhduc.courseservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.khanhduc.courseservice.dto.request.ChapterCreationRequest;
import vn.khanhduc.courseservice.dto.response.ChapterCreationResponse;
import vn.khanhduc.courseservice.dto.response.ResponseData;
import vn.khanhduc.courseservice.service.ChapterService;

@RestController
@RequestMapping("/chapters")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @PostMapping
    ResponseData<ChapterCreationResponse> creation(@RequestBody @Valid ChapterCreationRequest request) {
        return ResponseData.<ChapterCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .data(chapterService.create(request))
                .build();
    }
}
