package com.example.task.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000, nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonBackReference
    private Task task;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnore
    private User author;
}

