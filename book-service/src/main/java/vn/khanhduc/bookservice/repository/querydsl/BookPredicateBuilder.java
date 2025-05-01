package vn.khanhduc.bookservice.repository.querydsl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import vn.khanhduc.bookservice.dto.response.BookSearchRequest;
import vn.khanhduc.bookservice.entity.QBook;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BookPredicateBuilder {
    private final List<Predicate> predicates = new ArrayList<>();

    public BookPredicateBuilder(BookSearchRequest request) {
        if (request == null) {
            return;
        }
        QBook book = QBook.book;
        addKeywordCondition(request.getKeywords(), book.title::likeIgnoreCase); // same: book.title::likeIgnoreCase == s -> book.title.likeIgnoreCase(s)
        addKeywordCondition(request.getKeywords(), book.authorName::likeIgnoreCase);
        addKeywordCondition(request.getKeywords(), book.isbn::likeIgnoreCase);
        addKeywordCondition(request.getKeywords(), book.description::likeIgnoreCase);
        addKeywordCondition(request.getLanguage(), book.language::likeIgnoreCase);

        addRangeCondition(request.getMinPrice(), request.getMaxPrice(),
                book.price::goe, // trả về >=
                book.price::loe  // trả về <=
        );
    }

    private void addKeywordCondition(String keyword, Function<String, BooleanExpression> condition) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            String likePattern = "%" + keyword.trim() + "%";
            predicates.add(condition.apply(likePattern));
        }
    }

    private <T> void addRangeCondition(T min, T max, Function<T, BooleanExpression> minCondition, Function<T, BooleanExpression> maxCondition) {
        if(min != null) {
            predicates.add(minCondition.apply(min));
        }
        if(max != null) {
            predicates.add(maxCondition.apply(max));
        }
    }

    public Predicate build() {
        return predicates.isEmpty() ? null : ExpressionUtils.anyOf(predicates); // allOf: and, anyOf: or
    }

}
