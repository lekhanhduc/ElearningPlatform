package vn.khanhduc.cartservice.controller;

import vn.khanhduc.cartservice.dto.request.CartCreationRequest;
import vn.khanhduc.cartservice.dto.response.CartCreationResponse;
import vn.khanhduc.cartservice.dto.response.CartDetailResponse;
import vn.khanhduc.cartservice.dto.response.PageResponse;
import vn.khanhduc.cartservice.dto.response.ResponseData;
import vn.khanhduc.cartservice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    ResponseData<CartCreationResponse> createCart(@Valid @RequestBody CartCreationRequest request) {
        return ResponseData.<CartCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Created success")
                .data(cartService.creation(request))
                .build();
    }

    @GetMapping
    ResponseData<PageResponse<CartDetailResponse>> getAll(@RequestParam(required = false, defaultValue = "1") int page,
                                                          @RequestParam(required = false, defaultValue = "5") int size) {

        return ResponseData.<PageResponse<CartDetailResponse>>builder()
                .code(HttpStatus.OK.value())
                .data(cartService.getAll(page, size))
                .build();
    }

}
