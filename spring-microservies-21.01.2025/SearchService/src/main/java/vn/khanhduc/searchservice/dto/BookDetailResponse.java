package vn.khanhduc.searchservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDetailResponse implements Serializable {
    private String bookId;
    private String isbn;
    private String title;
    private String authorName;
    private String description;
    private String language;
    private Double price;
    private String bookCover;
}
