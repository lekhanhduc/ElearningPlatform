package vn.khanhduc.bookservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class BookSearchRequest implements Serializable {
    private String keywords;
    private String language;
    private String authorName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private int page = 1;
    private int size = 10;
    private String sortBy;

}
