package com.example.bookservice.controller;

import com.example.bookservice.dto.request.BookCreationRequest;
import com.example.bookservice.dto.response.BookCreationResponse;
import com.example.bookservice.dto.response.ResponseData;
import com.example.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    ResponseData<BookCreationResponse> createBook(
            @RequestPart(name = "request")BookCreationRequest request,
            @RequestPart(name = "bookCover") MultipartFile bookCover
            ) {
        var result = bookService.createBook(request, bookCover);
        return ResponseData.<BookCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .data(result)
                .build();
    }

}
