package com.bu.startup.service;

import com.bu.startup.entity.AssetBundleEntity;
import com.bu.startup.entity.Like;
import com.bu.startup.entity.User;
import com.bu.startup.repo.AssetBundleRepository;
import com.bu.startup.repo.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final AssetBundleRepository assetBundleRepository;

    @Transactional
    public boolean toggleLike(Long assetBundleId, User user) {
        AssetBundleEntity assetBundle = assetBundleRepository.findById(assetBundleId)
                .orElseThrow(() -> new IllegalArgumentException("AssetBundle not found"));

        Optional<Like> existingLike = likeRepository.findByAssetBundleAndUser(assetBundle, user);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return false; // Unliked
        } else {
            Like newLike = Like.builder()
                    .assetBundle(assetBundle)
                    .user(user)
                    .build();
            likeRepository.save(newLike);
            return true; // Liked
        }
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Long assetBundleId, User user) {
        AssetBundleEntity assetBundle = assetBundleRepository.findById(assetBundleId)
                .orElseThrow(() -> new IllegalArgumentException("AssetBundle not found"));
        return likeRepository.findByAssetBundleAndUser(assetBundle, user).isPresent();
    }

    @Transactional(readOnly = true)
    public long getLikeCount(Long assetBundleId) {
        AssetBundleEntity assetBundle = assetBundleRepository.findById(assetBundleId)
                .orElseThrow(() -> new IllegalArgumentException("AssetBundle not found"));
        return likeRepository.countByAssetBundle(assetBundle);
    }

    @Transactional(readOnly = true)
    public List<AssetBundleEntity> getLikedBundlesByUser(User user) {
        return likeRepository.findByUser(user).stream()
                .map(Like::getAssetBundle)
                .collect(Collectors.toList());
    }
}
