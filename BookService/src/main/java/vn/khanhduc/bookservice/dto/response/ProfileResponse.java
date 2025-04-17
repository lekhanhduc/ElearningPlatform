package vn.khanhduc.bookservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class ProfileResponse implements Serializable {
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String avatar;
}
