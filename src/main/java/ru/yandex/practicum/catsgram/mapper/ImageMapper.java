package ru.yandex.practicum.catsgram.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.catsgram.dto.NewImageRequest;
import ru.yandex.practicum.catsgram.dto.ImageDto;
import ru.yandex.practicum.catsgram.dto.UpdateImageRequest;
import ru.yandex.practicum.catsgram.model.Image;

import java.nio.file.Path;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageMapper {
    public static Image mapToImage(NewImageRequest request, Path filePath) {
        Image image = new Image();
        image.setPostId(request.getPostId());
        image.setOriginalFileName(request.getOriginalName());
        image.setFilePath(request.getFilePath());

        return image;
    }

    public static ImageDto mapToImageDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setPostId(image.getPostId());
        dto.setOriginalFileName(image.getOriginalFileName());
        dto.setFilePath(image.getFilePath());

        return dto;
    }

    public static Image updateImageFields(Image image, UpdateImageRequest request) {
        if (request.hasOriginalFileName()) {
            image.setOriginalFileName(request.getOriginalFileName());
        }
        if (request.hasFilePath()) {
            image.setFilePath(request.getFilePath());
        }

        return image;
    }
}