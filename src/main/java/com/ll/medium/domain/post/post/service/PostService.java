package com.ll.medium.domain.post.post.service;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.repository.PostRepository;
import com.ll.medium.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findAllByOrderByIdDesc(int count) {
        Pageable pageable = PageRequest.of(0, count);

        return postRepository.findAllByOrderByIdDesc(pageable);
    }

    public List<Post> findByIsPublishedOrderByIdDesc(boolean isPublished) {
        return postRepository.findByIsPublishedOrderByIdDesc(isPublished);
    }

    public List<Post> findByAuthorIdOrderByIdDesc(long authorId) {
        return postRepository.findByAuthorIdOrderByIdDesc(authorId);
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public Post write(Member author, String title, String body, boolean isPublished) {
        Post post = Post.builder()
                .author(author)
                .title(title)
                .body(body)
                .isPublished(isPublished)
                .build();
        postRepository.save(post);
        return post;
    }

    public boolean canModify(Member author, Post post) {
        if (author == null) return false;

        return post.getAuthor().equals(author);
    }

    @Transactional
    public void modify(Post post, String title, String body, boolean isPublished) {
        post.setTitle(title);
        post.setBody(body);
        post.setPublished(isPublished);
    }

    public boolean canDelete(Member author, Post post) {
        if (author == null) return false;

        if (author.isAdmin()) return true;

        return post.getAuthor().equals(author);
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

}
