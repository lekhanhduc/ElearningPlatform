package vn.khanhduc.searchservice.controller;

import vn.khanhduc.searchservice.dto.BookDetailResponse;
import vn.khanhduc.searchservice.dto.PageResponse;
import vn.khanhduc.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/books")
    PageResponse<BookDetailResponse> getBookWithSearch(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "") String keyword) {

        return searchService.getBookWithSearch(page, size, keyword);
    }

    @GetMapping("/books-async")
    Mono<PageResponse<BookDetailResponse>> getBookWithSearchAsync(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "") String keyword) {

        return searchService.getBookAndSearchWithWebClient(page, size, keyword);
    }

    @GetMapping("/books-java-api")
    PageResponse<BookDetailResponse> getBookWithJavaAPI(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "") String keyword) {

        return searchService.getBookWithJavaAPI(page, size, keyword);
    }
}
