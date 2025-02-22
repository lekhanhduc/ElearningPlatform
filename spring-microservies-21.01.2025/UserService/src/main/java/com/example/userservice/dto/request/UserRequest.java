package com.example.userservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class UserRequest implements Serializable {
    private Long userId;
    private String email;
    private String userStatus;
    private String firstName;
    private String lastName;
    private String avatar;
    private String phoneNumber;
}
