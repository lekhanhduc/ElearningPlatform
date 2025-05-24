package vn.khanhduc.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.khanhduc.courseservice.entity.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
}
