package com.example.bookservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class BookCreationRequest implements Serializable {

    @NotBlank(message = "Isbn cannot be blank")
    private String isbn;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Language cannot be blank")
    private String language;

    @NotNull(message = "Price cannot be blank")
    @Min(value = 1)
    private BigDecimal price;

    @NotNull(message = "Stock cannot be null")
    @Min(value = 0)
    private Integer stock;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // yyyy-MM-dd 2025-02-27
    private LocalDate publishDate;
}
