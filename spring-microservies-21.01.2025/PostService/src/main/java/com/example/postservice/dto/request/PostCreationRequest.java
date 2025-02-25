package com.example.postservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import java.io.Serializable;

@Getter
public class PostCreationRequest implements Serializable {
    @NotBlank(message = "Content cannot be blank")
    private String content;
}
