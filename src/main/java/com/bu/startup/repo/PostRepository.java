package com.bu.startup.repo;

import com.bu.startup.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    

    List<Post> findAllByAssetBundleId(Long assetBundleId);
}
