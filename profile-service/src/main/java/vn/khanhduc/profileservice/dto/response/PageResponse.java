package vn.khanhduc.profileservice.dto.response;

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
    private final int currentPage;
    private final int pageSize;
    private final int totalPages;
    private final Long totalElements;

    @Builder.Default
    private final List<T> data = Collections.emptyList();
}
