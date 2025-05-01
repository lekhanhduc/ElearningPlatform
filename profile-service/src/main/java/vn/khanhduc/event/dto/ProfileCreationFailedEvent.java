package vn.khanhduc.event.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileCreationFailedEvent implements Serializable {
    private Long userId;
}
