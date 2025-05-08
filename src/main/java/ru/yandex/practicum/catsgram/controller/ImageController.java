package ru.yandex.practicum.catsgram.controller;

import ru.yandex.practicum.catsgram.dto.ImageDto;
import ru.yandex.practicum.catsgram.dto.NewImageRequest;
import ru.yandex.practicum.catsgram.model.ImageData;
import ru.yandex.practicum.catsgram.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/posts/{postId}/images")
    public List<ImageDto> addPostImage(@PathVariable("postId") long imageId,
                                       @RequestParam("image")List<MultipartFile> files,
                                       @RequestBody NewImageRequest imageRequest) {
        return imageService.saveImages(imageId,files,imageRequest);
    }

    @GetMapping(value = "/images/{postId}/images")
    public List<ImageDto> getPostImages(@PathVariable("postId") Long postId) {
        return imageService.getPostImages(postId);
    }

    @GetMapping(value = "/images/{imageId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadImage(@PathVariable long imageId) {
        ImageData imageData = imageService.getImageData(imageId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(imageData.getName())
                        .build()
        );
        return new ResponseEntity<>(imageData.getData(), headers, HttpStatus.OK);
    }
}