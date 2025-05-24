package vn.khanhduc.profileservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDetailResponse {
    private final String profileId;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String avatar;
    private final String linkFacebook;
    private final String linkLinkedIn;
    private final String linkYoutube;
}
