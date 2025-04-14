package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class Image {
    /*Класс Image — модель, которая описывает изображения, прикреплённые к конкретному сообщению. У неё следующие поля данных:
    Long id — уникальный идентификатор изображения,
    long postId — уникальный идентификатор поста, к которому прикреплено изображение,
    String originalFileName — имя файла, который содержит изображение,
    String filePath — путь, по которому изображение было сохранено*/

    private Long id;
    private long postId;
    private String originalFileName;
    private String filePath;

}

