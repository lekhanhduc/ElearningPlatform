package com.example.searchservice.service;

import com.example.searchservice.dto.BookDetailResponse;
import com.example.searchservice.dto.PageResponse;
import com.example.searchservice.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SEARCH-SERVICE")
public class SearchService {

    private final SearchRepository searchRepository;

    public PageResponse<BookDetailResponse> getBookWithSearch(int page, int size, String keyword) {
        try {
            return searchRepository.getBookWithSearch(page, size, keyword);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Mono<PageResponse<BookDetailResponse>> getBookAndSearchWithWebClient(final int page, final int size, final String keyword) {
        return searchRepository.getBookWithSearchAsync(page, size, keyword);
    }

    public PageResponse<BookDetailResponse> getBookWithJavaAPI(int page, int size, String keyword) {
        return searchRepository.getBookWithJavaAPI(page, size, keyword);
    }

}
