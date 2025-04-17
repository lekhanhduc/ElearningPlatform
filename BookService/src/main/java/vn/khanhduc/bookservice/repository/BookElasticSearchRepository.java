package vn.khanhduc.bookservice.repository;

import vn.khanhduc.bookservice.entity.BookIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookElasticSearchRepository extends ElasticsearchRepository<BookIndex, Long> {
}
