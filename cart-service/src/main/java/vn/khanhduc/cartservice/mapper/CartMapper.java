package vn.khanhduc.cartservice.mapper;

import vn.khanhduc.cartservice.dto.response.CartDetailResponse;
import vn.khanhduc.cartservice.entity.Cart;

public class CartMapper {
    private CartMapper() {}

    public static CartDetailResponse cartDetailResponse(Cart cart) {
        return CartDetailResponse.builder()
                .cartId(cart.getId())
                .userId(cart.getUserId())
                .bookId(cart.getBookId())
                .bookTitle(cart.getBookTitle())
                .price(cart.getPrice())
                .quantity(cart.getQuantity())
                .totalPrice(cart.getTotalPrice())
                .build();
    }
}
