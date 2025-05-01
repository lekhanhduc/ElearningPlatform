package vn.khanhduc.bookservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class PageResponse<T> implements Serializable {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;

    @Builder.Default
    private List<T> data = Collections.emptyList();
}
