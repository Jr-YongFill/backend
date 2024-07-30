package com.yongfill.server.domain.file.entity;

import com.yongfill.server.domain.posts.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name="file")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "image_path", columnDefinition="Text", nullable = false)
    private String imagePath;

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne
    private Post post;


}
