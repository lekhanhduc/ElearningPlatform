package vn.khanhduc.courseservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.khanhduc.courseservice.dto.request.CategoryCreationRequest;
import vn.khanhduc.courseservice.dto.response.CategoryDetailResponse;
import vn.khanhduc.courseservice.dto.response.ResponseData;
import vn.khanhduc.courseservice.service.CategoryService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    ResponseData<Void> create(@RequestBody CategoryCreationRequest request) {
        categoryService.createCategory(request);
        return ResponseData.<Void>builder()
                .code(HttpStatus.CREATED.value())
                .build();
    }

    @GetMapping("/fetch-all")
    ResponseData<List<CategoryDetailResponse>> getAllCategories() {
        return ResponseData.<List<CategoryDetailResponse>>builder()
                .code(HttpStatus.OK.value())
                .data(categoryService.getAllCategories())
                .build();
    }

}
