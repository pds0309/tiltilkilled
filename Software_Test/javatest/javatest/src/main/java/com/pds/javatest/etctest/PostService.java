package com.pds.javatest.etctest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public Post set(String title, String contents) {
        return postRepository.save(Post.builder().title(title).content(contents).build());
    }

}
