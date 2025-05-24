package vn.khanhduc.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.khanhduc.courseservice.entity.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, String> {
}
