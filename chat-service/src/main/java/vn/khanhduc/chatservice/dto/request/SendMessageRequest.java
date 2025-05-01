package vn.khanhduc.chatservice.dto.request;

import vn.khanhduc.chatservice.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class SendMessageRequest implements Serializable {

    @NotNull(message = "Receiver id must not be null")
    private Long receiverId;

    @NotBlank(message = "Content must not be blank")
    private String content;

    private MessageType messageType;
}
