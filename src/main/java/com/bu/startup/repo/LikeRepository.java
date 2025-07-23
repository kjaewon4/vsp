package com.bu.startup.repo;

import com.bu.startup.entity.AssetBundleEntity;
import com.bu.startup.entity.Like;
import com.bu.startup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByAssetBundleAndUser(AssetBundleEntity assetBundle, User user);
    long countByAssetBundle(AssetBundleEntity assetBundle);
    List<Like> findByUser(User user);
}