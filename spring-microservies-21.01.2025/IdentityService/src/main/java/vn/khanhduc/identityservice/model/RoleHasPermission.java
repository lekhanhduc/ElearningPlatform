package vn.khanhduc.identityservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity(name = "RoleHasPermission")
@Table(name = "role_has_permission")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleHasPermission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
}
