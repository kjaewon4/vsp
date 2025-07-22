package com.bu.startup.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_bundle_id", unique = true) // One-to-one with AssetBundleEntity
    private AssetBundleEntity assetBundle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}
