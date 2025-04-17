package vn.khanhduc.bookservice.repository.querydsl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import vn.khanhduc.bookservice.dto.response.BookDetailResponse;
import vn.khanhduc.bookservice.dto.response.BookSearchRequest;
import vn.khanhduc.bookservice.dto.response.PageResponse;
import vn.khanhduc.bookservice.entity.Book;
import vn.khanhduc.bookservice.entity.QBook;
import java.util.List;

@Repository("jpaBookQueryRepository")
@RequiredArgsConstructor
@Slf4j(topic = "BOOK-QUERY-REPOSITORY")
public class BookQueryRepository {

    private final JPAQueryFactory queryFactory;

    public PageResponse<BookDetailResponse> searchBooks(BookSearchRequest request) {
        BookPredicateBuilder predicateBuilder = new BookPredicateBuilder(request);

        Predicate predicate = predicateBuilder.build();

        QBook book = QBook.book;

        Long count = queryFactory
                .select(book.count())
                .from(book)
                .where(predicate)
                .fetchOne();

        long totalElements = (count != null) ? count : 0;

        int page = (request != null) ? request.getPage() : 1;
        int size = (request != null) ? request.getSize() : 10;

        JPAQuery<Book> dataQuery = queryFactory.selectFrom(book)
                .where(predicate)
                .orderBy(new OrderSpecifier<>(Order.ASC, book.bookId))
                .offset((long) (page - 1) * size)
                .limit(size);

        List<Book> books = dataQuery.fetch();

        return PageResponse.<BookDetailResponse>builder()
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .data(books.stream().map(b -> BookDetailResponse.builder()
                                .bookId(b.getBookId())
                                .isbn(b.getIsbn())
                                .title(b.getTitle())
                                .description(b.getDescription())
                                .bookCover(b.getBookCover())
                                .price(b.getPrice())
                                .stock(b.getStock())
                                .language(b.getLanguage())
                                .authorName(b.getAuthorName())
                                .publishDate(b.getPublishDate())
                                .build())
                        .toList())
                .build();
    }

}
