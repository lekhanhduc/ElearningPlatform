package vn.khanhduc.bookservice.mapper;

import vn.khanhduc.bookservice.dto.request.BookCreationRequest;
import vn.khanhduc.bookservice.dto.response.BookCreationResponse;
import vn.khanhduc.bookservice.dto.response.BookDetailResponse;
import vn.khanhduc.bookservice.entity.Book;

public class BookMapper {
    private BookMapper() {}

    public static Book toBook(BookCreationRequest request) {
        return Book.builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .description(request.getDescription())
                .language(request.getLanguage())
                .price(request.getPrice())
                .stock(request.getStock())
                .publishDate(request.getPublishDate())
                .build();
    }

    public static BookCreationResponse toBookResponse(Book book) {
        return BookCreationResponse.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .description(book.getDescription())
                .language(book.getLanguage())
                .price(book.getPrice())
                .stock(book.getStock())
                .publishDate(book.getPublishDate())
                .bookCover(book.getBookCover())
                .build();
    }

    public static BookDetailResponse toBookDetailResponse(Book book) {
        return BookDetailResponse.builder()
                .bookId(book.getBookId())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .description(book.getDescription())
                .language(book.getLanguage())
                .price(book.getPrice())
                .stock(book.getStock())
                .publishDate(book.getPublishDate())
                .bookCover(book.getBookCover())
                .build();
    }
}
