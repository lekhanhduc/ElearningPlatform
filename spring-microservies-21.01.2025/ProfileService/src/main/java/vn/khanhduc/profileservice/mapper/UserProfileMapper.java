package vn.khanhduc.profileservice.mapper;

import org.mapstruct.Mapper;
import vn.khanhduc.profileservice.dto.request.ProfileRequest;
import vn.khanhduc.profileservice.dto.response.ProfileResponse;
import vn.khanhduc.profileservice.model.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileRequest request);
    ProfileResponse toProfileResponse(UserProfile userProfile);
}
