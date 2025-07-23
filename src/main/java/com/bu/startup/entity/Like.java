package com.bu.startup.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"asset_bundle_id", "user_id"})
}) // Prevent duplicate likes
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_bundle_id", nullable = false)
    private AssetBundleEntity assetBundle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
