package vn.khanhduc.identityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.khanhduc.identityservice.model.UserHasRole;

@Repository
public interface UserHasRoleRepository extends JpaRepository<UserHasRole, Long> {
}
