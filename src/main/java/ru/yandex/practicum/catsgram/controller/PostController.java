package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.enumeration.SortOrder;
import ru.yandex.practicum.catsgram.model.Image;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.ImageService;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final ImageService imageService;

    @Autowired
    public PostController(PostService postService, ImageService imageService) {
        this.postService = postService;
        this.imageService = imageService;
    }

    @GetMapping
    public Collection<Post> findAll(
            @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(value = "sort", defaultValue = "desc", required = false) String sort) {

        SortOrder sortOrder = SortOrder.from(sort);
        if(sortOrder == null){
            throw new IllegalArgumentException();
        }

        return postService.findAll(size, from, sortOrder);
    }


    @GetMapping("/{postId}")
    public Optional<Post> findPost(@PathVariable("postId") Long postId) {
        return postService.findPostById(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/posts/{postId}/images")
    public List<Image> addPostImages(@PathVariable("postId") long postId,
                                     @RequestParam("image") List<MultipartFile> files) {
        return imageService.saveImages(postId, files);
    }

}