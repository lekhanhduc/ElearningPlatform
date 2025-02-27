package com.example.bookservice.repository;

import com.example.bookservice.entity.BookIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookElasticSearchRepository extends ElasticsearchRepository<BookIndex, Long> {
}
