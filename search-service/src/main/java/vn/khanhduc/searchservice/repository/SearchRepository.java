package vn.khanhduc.searchservice.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import org.springframework.stereotype.Repository;
import vn.khanhduc.searchservice.dto.BookDetailResponse;
import vn.khanhduc.searchservice.dto.CourseResponse;
import vn.khanhduc.searchservice.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SearchRepository {

    private final ElasticsearchClient elasticsearchClient;
    private final WebClient webClient;

    public PageResponse<BookDetailResponse> getBookWithSearch(int page, int size, String keyword) throws IOException {
        int safePage = Math.max(page, 1);
        int from = (safePage - 1) * size;

        SearchResponse<BookDetailResponse> searchResponse = elasticsearchClient.search(s -> {
                    s.index("books").from(from).size(size);
                    if (!StringUtils.hasText(keyword)) {
                        s.query(q -> q.matchAll(m -> m));
                    } else {
                        s.query(q -> q
                                .bool(b -> b
                                        .should(m -> m.match(t -> t.field("title").query(keyword)))
                                        .should(m -> m.match(t -> t.field("authorName").query(keyword)))
                                        .should(m -> m.match(t -> t.field("description").query(keyword)))
                                        .should(m -> m.match(t -> t.field("language").query(keyword)))
                                )
                        );
                    }
                    return s;
                },
                BookDetailResponse.class
        );
        List<BookDetailResponse> books = searchResponse.hits().hits().stream()
                .map(Hit::source)
                .toList();

        long totalElements = searchResponse.hits().total() != null ? searchResponse.hits().total().value() : 0L;
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return PageResponse.<BookDetailResponse>builder()
                .currentPage(safePage)
                .pageSize(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .data(books)
                .build();
    }

    public PageResponse<CourseResponse> getCourseWithSearch(int page, int size, String keyword) {
        if(page <= 0 ) {
            page = 1;
        }
        SearchRequest.Builder searchBuilder  = new SearchRequest.Builder()
                .index("courses")
                .from(page - 1)
                .size(size);

        if(StringUtils.hasText(keyword)) {
            searchBuilder.query(q -> q.multiMatch(m -> m.query(keyword)
                                    .fields("name", "description", "categoryName")
                                    .fuzziness("AUTO")
            ));
        } else {
            searchBuilder.query(q -> q.matchAll(m -> m));
        }

        try {
            SearchResponse<CourseResponse> searchResponse = elasticsearchClient.search(searchBuilder.build(), CourseResponse.class);
            List<CourseResponse> courses = searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

            long totalElements = searchResponse.hits().total() != null ? searchResponse.hits().total().value() : 0L;
            int totalPages = (int) Math.ceil((double) totalElements / size);

            return PageResponse.<CourseResponse>builder()
                    .currentPage(page)
                    .pageSize(size)
                    .totalPages(totalPages)
                    .totalElements(totalElements)
                    .data(courses)
                    .build();
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PageResponse<BookDetailResponse> getBookWithJavaAPI(int page, int size, String keyword) {
        if(page <= 0 ) {
            page = 1;
        }
        SearchRequest.Builder searchBuilder  = new SearchRequest.Builder()
                .index("books")
                .from(page - 1)
                .size(size);

        if(StringUtils.hasText(keyword)) {
            searchBuilder.query(q -> q.multiMatch(m -> m.query(keyword)
                                    .fields("title", "authorName", "description", "language")
                                    .fuzziness("AUTO")
//                            .minimumShouldMatch("60%")
                    )
            );
        } else {
            searchBuilder.query(q -> q.matchAll(m -> m));
        }

        try {
            SearchResponse<BookDetailResponse> searchResponse =
                    elasticsearchClient.search(searchBuilder.build(), BookDetailResponse.class);
            List<BookDetailResponse> books = searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

            long totalElements = Optional.ofNullable(searchResponse.hits().total()) // Total Hits
                    .map(TotalHits::value)
                    .orElse(0L);
            int totalPages = (int) Math.ceil((double) totalElements / size);

            return PageResponse.<BookDetailResponse>builder()
                    .currentPage(page)
                    .pageSize(size)
                    .totalPages(totalPages)
                    .totalElements(totalElements)
                    .data(books)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Mono<PageResponse<BookDetailResponse>> getBookWithSearchAsync(int page, int size, String keyword) {
        String query;
        if (StringUtils.hasText(keyword)) {
            query = """
        {
            "query": {
                "multi_match": {
                    "query": "%s",
                    "fields": ["title", "authorName", "description", "language"],
                    "fuzziness": "AUTO"
                }
            },
            "from": %d,
            "size": %d
        }
        """.formatted(keyword, (page - 1) * size, size);
        } else {
            query = """
        {
            "query": {
                "match_all": {}
            },
            "from": %d,
            "size": %d
        }
        """.formatted((page - 1) * size, size);
        }

        return webClient.post()
                .uri("/books/_search")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(response -> {
                    // map về Object Java vì ElasticSearch trả dữ liệu về Json: Key: Value nên dùng List<Map<String, Object>> để mapping data
                    List<Map<String, Object>> hits = (List<Map<String, Object>>)
                            ((Map<String, Object>) response.get("hits")).get("hits");

                    List<BookDetailResponse> books = hits.stream()
                            .map(hit -> {
                                Map<String, Object> source = (Map<String, Object>) hit.get("_source");
                                return BookDetailResponse.builder()
                                        .bookId(String.valueOf(source.get("bookId")))
                                        .isbn((String) source.get("isbn"))
                                        .title((String) source.get("title"))
                                        .authorName((String) source.get("authorName"))
                                        .description((String) source.get("description"))
                                        .language((String) source.get("language"))
                                        .price(source.get("price") != null ? ((Number) source.get("price")).doubleValue() : null)
                                        .bookCover((String) source.get("bookCover"))
                                        .build();
                            })
                            .toList();

                    Map<String, Object> total = (Map<String, Object>) ((Map<String, Object>) response.get("hits")).get("total");
                    long totalElements = total.get("value") != null ? ((Number) total.get("value")).longValue() : 0L;
                    int totalPages = (int) Math.ceil((double) totalElements / size);

                    return PageResponse.<BookDetailResponse>builder()
                            .currentPage(page)
                            .pageSize(size)
                            .totalPages(totalPages)
                            .totalElements(totalElements)
                            .data(books)
                            .build();
                });
    }


}
