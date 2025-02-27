package com.example.bookservice.service.impl;

import com.example.bookservice.dto.request.BookCreationRequest;
import com.example.bookservice.dto.response.BookCreationResponse;
import com.example.bookservice.entity.Book;
import com.example.bookservice.exception.BookException;
import com.example.bookservice.exception.ErrorCode;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.BookService;
import com.example.bookservice.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "BOOK-SERVICE")
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CloudinaryService cloudinaryService;

//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Override
    public BookCreationResponse createBook(BookCreationRequest request, MultipartFile bookCover) {
        var principle = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(principle.getName());
        Book book = Book.builder()
                .userId(userId)
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .description(request.getDescription())
                .language(request.getLanguage())
                .price(request.getPrice())
                .stock(request.getStock())
                .publishDate(request.getPublishDate())
                .build();

        final String bookCoverUrl = cloudinaryService.uploadImage(bookCover);
        book.setBookCover(bookCoverUrl);
        try {
            bookRepository.save(book);
        }catch (DataIntegrityViolationException e) {
            log.error("Book creation failed: {}", e.getMessage());
            throw new BookException(ErrorCode.BOOK_EXISTED);
        }
        return BookCreationResponse.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .description(book.getDescription())
                .language(book.getLanguage())
                .price(book.getPrice())
                .stock(book.getStock())
                .publishDate(book.getPublishDate())
                .bookCover(bookCoverUrl)
                .build();
    }

}
