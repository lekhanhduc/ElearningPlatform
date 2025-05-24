package vn.khanhduc.identityservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import java.io.Serializable;

@Getter
public class ChangePasswordRequest implements Serializable {

    @NotBlank(message = "Old password cannot be blank")
    private String oldPassword;

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 6, message = "Password must be 6 characters") // size dùng cho String, Min dùng cho numeric
    private String newPassword;
}
