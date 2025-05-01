package vn.khanhduc.bookservice.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

@Document(indexName = "books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookIndex implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Field(type = FieldType.Text)
    private String bookId;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard") // dữ liệu như nào search như đó
    private String authorName;

    @Field(type = FieldType.Keyword)  // Không cần full-text search, chỉ tìm chính xác
    private String isbn;

    @Field(type = FieldType.Text, analyzer = "english")  // Hỗ trợ full-text search
    private String title;

    @Field(type = FieldType.Text, analyzer = "english")  // Hỗ trợ full-text search
    private String description;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Keyword)
    private String language;

    @Field(type = FieldType.Text)
    private String bookCover;

}
