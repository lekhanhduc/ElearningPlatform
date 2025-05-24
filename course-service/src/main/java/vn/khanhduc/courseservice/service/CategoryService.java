package vn.khanhduc.courseservice.service;

import vn.khanhduc.courseservice.dto.request.CategoryCreationRequest;
import vn.khanhduc.courseservice.dto.response.CategoryDetailResponse;

import java.util.List;

public interface CategoryService {
    void createCategory(CategoryCreationRequest request);
    List<CategoryDetailResponse> getAllCategories();
}
