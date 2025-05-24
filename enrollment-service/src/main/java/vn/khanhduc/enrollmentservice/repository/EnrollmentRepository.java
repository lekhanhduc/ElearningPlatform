package vn.khanhduc.enrollmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.khanhduc.enrollmentservice.entity.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {

    @Query("select case when count(e) > 0 THEN true ELSE false END from Enrollment e WHERE e.userId = :userId and e.courseId = :courseId")
    boolean checkExistByUserIdAndCourseId(Long userId, String courseId);
}
