package vn.khanhduc.cartservice.service;

import vn.khanhduc.cartservice.dto.request.CartCreationRequest;
import vn.khanhduc.cartservice.dto.response.CartCreationResponse;
import vn.khanhduc.cartservice.dto.response.CartDetailResponse;
import vn.khanhduc.cartservice.dto.response.PageResponse;

public interface CartService {
    CartCreationResponse creation(CartCreationRequest request);
    PageResponse<CartDetailResponse> getAll(int page, int size);
}
