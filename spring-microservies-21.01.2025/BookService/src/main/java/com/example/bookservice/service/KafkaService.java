package com.example.bookservice.service;

import com.example.bookservice.entity.Book;
import com.example.bookservice.exception.BookException;
import com.example.bookservice.exception.ErrorCode;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.repository.httpclient.UserClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "KAFKA-SERVICE")
public class KafkaService {

    private final BookRepository bookRepository;
    private final UserClient userClient;

    @KafkaListener(topics = "book-creation", groupId = "book-group")
    public void saveBook(Book book, Acknowledgment acknowledgment){
        try {
            var authorName = userClient.getProfileByUserId(book.getUserId());
            book.setAuthorName(String.format("%s %s", authorName.getFirstName(), authorName.getLastName()));
            bookRepository.save(book);
            log.info("Book saved: {}", book);
            acknowledgment.acknowledge();
        }catch (DataIntegrityViolationException e){
            log.error("Error while saving book", e);
            throw new BookException(ErrorCode.BOOK_EXISTED);
        }catch (FeignException e){
            throw new BookException(ErrorCode.FEIGN_ERROR);
        }
    }

}
