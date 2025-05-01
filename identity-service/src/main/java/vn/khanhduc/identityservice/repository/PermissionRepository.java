package vn.khanhduc.identityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.khanhduc.identityservice.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
