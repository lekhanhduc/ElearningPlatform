package vn.khanhduc.courseservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.khanhduc.courseservice.dto.request.CategoryCreationRequest;
import vn.khanhduc.courseservice.dto.response.CategoryDetailResponse;
import vn.khanhduc.courseservice.entity.Category;
import vn.khanhduc.courseservice.repository.CategoryRepository;
import vn.khanhduc.courseservice.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CATEGORY-SERVICE")
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void createCategory(CategoryCreationRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .build();
        categoryRepository.save(category);
        log.info("Create category successfully. Category name: {}", category.getName());
    }

    @Override
    public List<CategoryDetailResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> CategoryDetailResponse.builder()
                        .categoryId(category.getId())
                        .name(category.getName())
                        .build())
                .toList();
    }
}
