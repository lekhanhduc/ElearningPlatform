package com.example.bookservice.service;

import com.example.bookservice.dto.request.BookCreationRequest;
import com.example.bookservice.dto.response.BookDetailResponse;
import com.example.bookservice.dto.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
    void createBook(BookCreationRequest request, MultipartFile bookCover);
    PageResponse<BookDetailResponse> getAll(int page, int size);
}
