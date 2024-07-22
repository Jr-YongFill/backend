package com.yongfill.server.domain.stack.entity;

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
public class QuestionStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stack_name", length = 100, nullable = false)
    private String stackName;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "description", length = 500, nullable = false)
    private String description;

}
