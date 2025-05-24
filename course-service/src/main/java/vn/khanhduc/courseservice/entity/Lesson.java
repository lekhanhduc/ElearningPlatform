package vn.khanhduc.courseservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Lesson")
@Table(name = "lesson")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Lesson extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "lesson_name", nullable = false)
    private String name;

    @Column(name = "lesson_description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "lesson_url", nullable = false)
    private String lessonUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;

}
