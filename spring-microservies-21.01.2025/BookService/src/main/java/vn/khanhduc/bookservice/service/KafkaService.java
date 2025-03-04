package vn.khanhduc.bookservice.service;

import vn.khanhduc.bookservice.entity.Book;
import vn.khanhduc.bookservice.entity.BookIndex;
import vn.khanhduc.bookservice.exception.BookException;
import vn.khanhduc.bookservice.exception.ErrorCode;
import vn.khanhduc.bookservice.repository.BookElasticSearchRepository;
import vn.khanhduc.bookservice.repository.BookRepository;
import vn.khanhduc.bookservice.repository.httpclient.UserClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "KAFKA-SERVICE")
public class KafkaService {

    private final BookRepository bookRepository;
    private final UserClient userClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final BookElasticSearchRepository bookElasticSearchRepository;

    @KafkaListener(topics = "book-creation", groupId = "book-group")
    public void saveBook(Book book, Acknowledgment acknowledgment){
        try {
            var authorName = userClient.getProfileByUserId(book.getUserId());
            book.setAuthorName(String.format("%s %s", authorName.getFirstName(), authorName.getLastName()));
            bookRepository.save(book);
            log.info("Book saved: {}", book);

            BookIndex bookElasticSearch = BookIndex.builder()
                    .bookId(book.getBookId())
                    .isbn(book.getIsbn())
                    .title(book.getTitle())
                    .description(book.getDescription())
                    .price(book.getPrice())
                    .language(book.getLanguage())
                    .authorName(book.getAuthorName())
                    .build();

            kafkaTemplate.send("book-to-elastic-search", bookElasticSearch);
            acknowledgment.acknowledge();
        }catch (DataIntegrityViolationException e){
            log.error("Error while saving book", e);
            throw new BookException(ErrorCode.BOOK_EXISTED);
        }catch (FeignException e){
            throw new BookException(ErrorCode.FEIGN_ERROR);
        }
    }

    @KafkaListener(topics = "book-to-elastic-search", groupId = "book-group")
    public void saveBookToElasticSearch(BookIndex bookIndex, Acknowledgment acknowledgment){
        log.info("Book saved to elastic search: {}", bookIndex.toString());
        try {
            bookElasticSearchRepository.save(bookIndex);
            acknowledgment.acknowledge();
        }catch (Exception e){
            log.error("Error while saving book to elastic search", e);
            throw new BookException(ErrorCode.SAVE_ELASTIC_SEARCH_ERROR);
        }
    }

}
