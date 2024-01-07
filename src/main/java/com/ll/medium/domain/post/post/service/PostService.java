package com.ll.medium.domain.post.post.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.dto.PostControlPermissionDto;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    public List<Post> findTop30ByIsPublishedOrderByIdDesc(boolean isPublished) {
        return postRepository.findTop30ByIsPublishedOrderByIdDesc(isPublished);
    }

    public Page<Post> findByIsPublishedOrderByIdDesc(boolean isPublished, Pageable pageable) {
        return postRepository.findByIsPublishedOrderByIdDesc(isPublished, pageable);
    }

    public Page<Post> findByAuthorIdOrderByIdDesc(long authorId, Pageable pageable) {
        return postRepository.findByAuthorIdOrderByIdDesc(authorId, pageable);
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public Optional<Post> findByIdAndAuthorId(long id, long authorId) {
        return postRepository.findByIdAndAuthorId(id, authorId);
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

    public boolean canAccess(Member author, Post post) {
        if (post.isPublished()) return true;

        if (author == null) return false;

        if (author.isAdmin()) return true;

        return post.getAuthor().equals(author);
    }

    @Transactional
    public void setIsPaid(Post post, boolean isPaid) {
        post.setPaid(isPaid);
    }

    public PostControlPermissionDto getContolPermissions(Member author, Post post) {
        return new PostControlPermissionDto(
                canModify(author, post),
                canDelete(author, post)
        );
    }
}
