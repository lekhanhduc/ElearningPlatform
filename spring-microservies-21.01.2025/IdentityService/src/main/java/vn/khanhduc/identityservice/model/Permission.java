package vn.khanhduc.identityservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.Set;

@Entity(name = "Permission")
@Table(name = "permission")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "permission")
    private Set<RoleHasPermission> roleHasPermissions;
}
