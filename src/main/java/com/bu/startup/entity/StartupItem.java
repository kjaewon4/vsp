package com.bu.startup.entity;

import com.bu.startup.type.ItemStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.bu.startup.type.CategoryType;

@Entity
@Table(name = "startup_items")
@Getter
@Setter
@NoArgsConstructor
public class StartupItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CategoryType category;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ItemStatus status;

    private Integer views;
}
