package vn.khanhduc.bookservice.service.impl;

import vn.khanhduc.bookservice.dto.request.BookCreationRequest;
import vn.khanhduc.bookservice.dto.response.BookDetailResponse;
import vn.khanhduc.bookservice.dto.response.PageResponse;
import vn.khanhduc.bookservice.entity.Book;
import vn.khanhduc.bookservice.mapper.BookMapper;
import vn.khanhduc.bookservice.repository.BookRepository;
import vn.khanhduc.bookservice.service.BookService;
import vn.khanhduc.bookservice.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "BOOK-SERVICE")
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CloudinaryService cloudinaryService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void createBook(BookCreationRequest request, MultipartFile bookCover) {
        var principle = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(principle.getName());
        Book book = BookMapper.toBook(request);
        book.setUserId(userId);
        final String bookCoverUrl = cloudinaryService.uploadImage(bookCover);
        book.setBookCover(bookCoverUrl);
        kafkaTemplate.send("book-creation", book);
    }

    @Override
    public PageResponse<BookDetailResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "title"));
        Page<Book> pageData = bookRepository.findAll(pageable);

        return PageResponse.<BookDetailResponse>builder()
                .page(page)
                .size(pageable.getPageSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(BookMapper::toBookDetailResponse).toList())
                .build();
    }

}
