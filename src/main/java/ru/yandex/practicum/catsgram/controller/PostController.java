package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.dto.*;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@RequestBody NewPostRequest postRequest) {
        return postService.createPost(postRequest);
    }

    @PutMapping("/{postId}")
    public PostDto updatePost(@PathVariable("postId") long postId, @RequestBody UpdatePostRequest request) {
        return postService.updatePost(postId, request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getPosts(
            @RequestParam(value = "sort", defaultValue = "desc", required = false) String sort,
            @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {

        switch (sort.toLowerCase()) {
            case "ascending":
            case "asc":
                sort = "ASC";
                break;
            case "descending":
            case "desc":
                sort = "DESC";
                break;
            default: sort = "FALSE";
        }

        if (sort.equals("FALSE")) {
            throw new IllegalArgumentException();
        }

        return postService.getPosts(sort, from, size);
    }

    @GetMapping("/{postId}")
    public Optional<PostDto> findPost(@PathVariable("postId") Long postId) {
        return postService.getPostById(postId);
    }

    // По автору
    @GetMapping("/author/{authorId}")
    public List<PostDto> findPostsByAuthor(@PathVariable("authorId") Long authorId) {
        return postService.getPostsByUserId(authorId);
    }
}