package vn.khanhduc.profileservice.dto.request;

import lombok.Getter;
import java.io.Serializable;

@Getter
public class ProfileRequest implements Serializable {
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
