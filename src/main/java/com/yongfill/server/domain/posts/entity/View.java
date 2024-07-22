package com.yongfill.server.domain.posts.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
public class View {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


}
