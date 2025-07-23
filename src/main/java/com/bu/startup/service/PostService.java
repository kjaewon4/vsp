package com.bu.startup.service;

import com.bu.startup.entity.AssetBundleEntity;
import com.bu.startup.entity.Post;
import com.bu.startup.entity.User;
import com.bu.startup.repo.AssetBundleRepository;
import com.bu.startup.repo.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AssetBundleRepository assetBundleRepository;

    @Transactional
    public Post createPost(Long assetBundleId, String title, String content, User author) {
        AssetBundleEntity assetBundle = assetBundleRepository.findById(assetBundleId)
                .orElseThrow(() -> new IllegalArgumentException("AssetBundle not found"));

        Post newPost = Post.builder()
                .assetBundle(assetBundle)
                .title(title)
                .author(author)
                .content(content)
                .build();
        return postRepository.save(newPost);
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByAssetBundleId(Long assetBundleId) {
        return postRepository.findAllByAssetBundleId(assetBundleId);
    }

    @Transactional
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

    @Transactional
    public Post updatePost(Long postId, String title, String content, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getAuthor().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("You are not authorized to update this post.");
        }

        post.setTitle(title);
        post.setContent(content);
        return postRepository.save(post);
    }
}
