package vn.khanhduc.searchservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDetailResponse implements Serializable {

    @JsonProperty(value = "bookId")
    private String bookId;

    @JsonProperty(value = "authorName")
    private String authorName;

    @JsonProperty(value = "isbn")
    private String isbn;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "language")
    private String language;

    @JsonProperty(value = "price")
    private Double price;

    @JsonProperty(value = "bookCover")
    private String bookCover;
}
