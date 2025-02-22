package com.example.userservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class UserDetailResponse implements Serializable {
    private String email;
    private String userStatus;
    private String firstName;
    private String lastName;
    private String avatar;
    private String phoneNumber;
}
