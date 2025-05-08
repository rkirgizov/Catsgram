package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.catsgram.dal.ImageRepository;
import ru.yandex.practicum.catsgram.dto.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.ImageFileException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.mapper.ImageMapper;
import ru.yandex.practicum.catsgram.model.Image;
import ru.yandex.practicum.catsgram.model.ImageData;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final PostService postService;

    // Укажите директорию для хранения изображений
    private final String imageDirectory = "${catsgram.image-directory}";

    @Autowired
    public ImageService(ImageRepository imageRepository, PostService postService) {
        this.imageRepository = imageRepository;
        this.postService = postService;
    }

    public List<ImageDto> saveImages(long postId, List<MultipartFile> files, NewImageRequest request) {
        return files.stream().map(file -> saveImage(postId, file, request)).collect(Collectors.toList());
    }

    public ImageDto saveImage(long postId, MultipartFile file, NewImageRequest request) {
        PostDto postDto = postService.getPostById(postId)
                .orElseThrow(() -> new ConditionsNotMetException("Указанный пост не найден"));
        Path filePath = saveFile(file, postDto);
        Image image = ImageMapper.mapToImage(request, filePath);
        image = imageRepository.save(image);

        return ImageMapper.mapToImageDto(image);
    }

    public Optional<ImageDto> getImageById(long imageId) {
        return Optional.ofNullable(imageRepository.findById(imageId)
                .map(ImageMapper::mapToImageDto)
                .orElseThrow(() -> new NotFoundException("Изображение не найдено с ID: " + imageId)));
    }

    public List<ImageDto> getPostImages(Long postId) {
        return imageRepository.findByPostId(postId)
                .stream()
                .map(ImageMapper::mapToImageDto)
                .collect(Collectors.toList());
    }

    public ImageDto updateImage(long imageId, UpdateImageRequest request) {
        Image updatedImage = imageRepository.findById(imageId)
                .map(image -> ImageMapper.updateImageFields(image, request))
                .orElseThrow(() -> new NotFoundException("Изображение не найдено"));
        updatedImage = imageRepository.update(updatedImage);
        return ImageMapper.mapToImageDto(updatedImage);
    }

    public ImageData getImageData(long imageId) {
        Image imageForResponse = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Изображение не найдено"));
        byte[] date = loadFile(imageForResponse);
        return new ImageData(date, imageForResponse.getOriginalFileName());
    }

    private Path saveFile(MultipartFile file, PostDto postDto) {
        try {
            String uniqueFileName = String.format("%d.%s", Instant.now().toEpochMilli(),
                    StringUtils.getFilenameExtension(file.getOriginalFilename()));
            Path uploadPath = Paths.get(imageDirectory, String.valueOf(postDto.getAuthorId()), postDto.getId().toString());
            Path filePath = uploadPath.resolve(uniqueFileName);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            file.transferTo(filePath);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private byte[] loadFile(Image image) {
        Path path = Paths.get(image.getFilePath());
        if (Files.exists(path)) {
            try {
                return Files.readAllBytes(path);
            } catch (IOException e) {
                throw new ImageFileException("Ошибка чтения файла.  Id: " + image.getId()
                        + ", name: " + image.getOriginalFileName(), e);
            }
        } else {
            throw new ImageFileException("Файл не найден. Id: " + image.getId()
                    + ", name: " + image.getOriginalFileName());
        }
    }


}