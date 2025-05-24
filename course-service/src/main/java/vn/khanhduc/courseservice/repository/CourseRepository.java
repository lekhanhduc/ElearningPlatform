package vn.khanhduc.courseservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.khanhduc.courseservice.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    @Query("SELECT c FROM Course c WHERE c.category.name LIKE %:categoryName%")
    Page<Course> findCourseByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);

}
