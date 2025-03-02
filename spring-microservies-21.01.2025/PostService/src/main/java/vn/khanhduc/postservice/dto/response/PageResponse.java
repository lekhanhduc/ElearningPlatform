package vn.khanhduc.postservice.dto.response;

import lombok.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> implements Serializable {
    private int currentPage;
    private int size;
    private int totalPages;
    private long totalElements;
    @Builder.Default
    private List<T> data = Collections.emptyList();
}
