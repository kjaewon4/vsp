package com.bu.startup.service;

import com.bu.startup.entity.AssetBundleEntity;
import com.bu.startup.entity.Post;
import com.bu.startup.entity.User;
import com.bu.startup.repo.AssetBundleRepository;
import com.bu.startup.repo.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AssetBundleRepository assetBundleRepository;

    @Transactional
    public Post createOrUpdatePost(Long assetBundleId, String content, User author) {
        AssetBundleEntity assetBundle = assetBundleRepository.findById(assetBundleId)
                .orElseThrow(() -> new IllegalArgumentException("AssetBundle not found"));

        Optional<Post> existingPost = postRepository.findByAssetBundleId(assetBundleId);

        if (existingPost.isPresent()) {
            Post post = existingPost.get();
            post.setContent(content);
            post.setAuthor(author); // Update author if needed, or keep original
            return postRepository.save(post);
        } else {
            Post newPost = Post.builder()
                    .assetBundle(assetBundle)
                    .author(author)
                    .content(content)
                    .build();
            return postRepository.save(newPost);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPostByAssetBundleId(Long assetBundleId) {
        return postRepository.findByAssetBundleId(assetBundleId);
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    @Transactional
    public void deletePost(Long postId, User currentUser, boolean isAdmin) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (isAdmin || post.getAuthor().getUserId().equals(currentUser.getUserId())) {
            postRepository.delete(post);
        } else {
            throw new IllegalArgumentException("You are not authorized to delete this post.");
        }
    }
}
