package com.pds.javatest.etctest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<Post> getAll() {
        return postService.getAll();
    }

    @PostMapping
    public Long set(@RequestBody PostDto postDto) {
        return postService.set(postDto.getTitle(), postDto.getContents()).getId();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class PostDto {
        String title;
        String contents;
    }
}
