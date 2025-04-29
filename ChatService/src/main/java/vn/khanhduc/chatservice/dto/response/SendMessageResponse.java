package vn.khanhduc.chatservice.dto.response;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageResponse implements Serializable {
    private Long messageId;
    private Long conversationId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String messageType;
    private LocalDateTime createdAt;
}
