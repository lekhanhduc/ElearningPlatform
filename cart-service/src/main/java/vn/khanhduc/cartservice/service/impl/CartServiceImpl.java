package vn.khanhduc.cartservice.service.impl;

import vn.khanhduc.cartservice.dto.request.CartCreationRequest;
import vn.khanhduc.cartservice.dto.response.CartCreationResponse;
import vn.khanhduc.cartservice.dto.response.CartDetailResponse;
import vn.khanhduc.cartservice.dto.response.PageResponse;
import vn.khanhduc.cartservice.entity.Cart;
import vn.khanhduc.cartservice.exception.CartException;
import vn.khanhduc.cartservice.exception.ErrorCode;
import vn.khanhduc.cartservice.mapper.CartMapper;
import vn.khanhduc.cartservice.repository.CartRepository;
import vn.khanhduc.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CART-SERVICE")
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @PreAuthorize("isAuthenticated()")
    @Override
    public CartCreationResponse creation(CartCreationRequest request) {
        Optional<String> principal = SecurityContextHolder.getContext().getAuthentication().getName().describeConstable();
        if(principal.isEmpty()) {
            throw new CartException(ErrorCode.UNAUTHENTICATED);
        }
        Long userId = Long.parseLong(principal.get());

        Cart cart = Cart.builder()
                .userId(userId)
                .bookId(request.getBookId())
                .bookTitle(request.getBookTitle())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();

        cartRepository.save(cart);

        return CartCreationResponse.builder()
                .userId(userId)
                .bookId(cart.getBookId())
                .bookTitle(cart.getBookTitle())
                .price(cart.getPrice())
                .quantity(cart.getQuantity())
                .totalPrice(cart.getTotalPrice())
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public PageResponse<CartDetailResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Cart> cartPage = cartRepository.findAll(pageable);
        List<Cart> carts = cartPage.getContent();
        return PageResponse.<CartDetailResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalPages(cartPage.getTotalPages())
                .totalElements(cartPage.getTotalElements())
                .data(carts.stream()
                        .map(CartMapper::cartDetailResponse)
                        .toList())
                .build();
    }

}
