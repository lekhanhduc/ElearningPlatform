package vn.khanhduc.profileservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vn.khanhduc.profileservice.dto.request.ProfileRequest;
import vn.khanhduc.profileservice.dto.request.ProfileUpdateRequest;
import vn.khanhduc.profileservice.dto.response.ProfileResponse;
import vn.khanhduc.profileservice.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileRequest request);
    ProfileResponse toProfileResponse(UserProfile userProfile);
    void updateProfile(ProfileUpdateRequest request, @MappingTarget UserProfile userProfile);
}
