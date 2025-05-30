package vn.khanhduc.bookservice.service;

import vn.khanhduc.bookservice.dto.request.BookCreationRequest;
import vn.khanhduc.bookservice.dto.response.BookDetailResponse;
import vn.khanhduc.bookservice.dto.response.BookSearchRequest;
import vn.khanhduc.bookservice.dto.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
    void createBook(BookCreationRequest request, MultipartFile bookCover);
    BookDetailResponse getBookDetailById(String bookId);
    PageResponse<BookDetailResponse> getAll(int page, int size);
    PageResponse<BookDetailResponse> searchBooks(BookSearchRequest request);
}
