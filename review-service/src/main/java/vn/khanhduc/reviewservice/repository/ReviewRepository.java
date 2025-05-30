package vn.khanhduc.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.khanhduc.reviewservice.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
