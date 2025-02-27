package com.example.bookservice.service;

import com.example.bookservice.dto.request.BookCreationRequest;
import com.example.bookservice.dto.response.BookCreationResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
    BookCreationResponse createBook(BookCreationRequest request, MultipartFile bookCover);
}
