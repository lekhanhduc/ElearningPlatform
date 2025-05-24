package vn.khanhduc.profileservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class ProfileUpdateRequest implements Serializable {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String linkFacebook;
    private String linkLinkedIn;
    private String linkYoutube;
}
