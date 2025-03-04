package vn.khanhduc.bookservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class BookDetailResponse implements Serializable {
    private String isbn;
    private String title;
    private String authorName;
    private String description;
    private String language;
    private BigDecimal price;
    private Integer stock;
    private LocalDate publishDate;
    private String bookCover;
}
