package vn.khanhduc.bookservice.controller;

import vn.khanhduc.bookservice.dto.request.BookCreationRequest;
import vn.khanhduc.bookservice.dto.response.*;
import vn.khanhduc.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/creation")
    ResponseData<BookCreationResponse> createBook(
            @RequestPart(name = "request")BookCreationRequest request,
            @RequestPart(name = "bookCover") MultipartFile bookCover
            ) {
        bookService.createBook(request, bookCover);
        return ResponseData.<BookCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Book created successfully")
                .build();
    }

    @GetMapping("/detail/{bookId}")
    ResponseData<BookDetailResponse> getBook(@PathVariable String bookId) {
        var result = bookService.getBookDetailById(bookId);
        return ResponseData.<BookDetailResponse>builder()
                .code(HttpStatus.OK.value())
                .data(result)
                .build();
    }

    @GetMapping("/fetch-all")
    ResponseData<PageResponse<BookDetailResponse>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        var result = bookService.getAll(page, size);
        return ResponseData.<PageResponse<BookDetailResponse>>builder()
                .code(HttpStatus.OK.value())
                .data(result)
                .build();
    }

    @PostMapping("/find-all-with-querydsl")
    ResponseData<PageResponse<BookDetailResponse>> getAll(
           @RequestBody(required = false) BookSearchRequest request
    ) {
        var result = bookService.searchBooks(request);
        return ResponseData.<PageResponse<BookDetailResponse>>builder()
                .code(HttpStatus.OK.value())
                .data(result)
                .build();
    }

}
