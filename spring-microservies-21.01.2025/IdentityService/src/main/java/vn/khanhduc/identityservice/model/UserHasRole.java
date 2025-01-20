package vn.khanhduc.identityservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity(name = "UserHasRole")
@Table(name = "user_has_role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserHasRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
