package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = "id")
public class Post {
    /*Класс Post — модель, описывающая сообщения в социальной сети. У неё следующие поля данных:
    Long id — уникальный идентификатор сообщения,
    long authorId — пользователь, который создал сообщение,
    String description — текстовое описание сообщения,
    Instant postDate — дата и время создания сообщения.*/
    public Long id;
    public Long authorId;
    public String description;
    public Instant postDate;
}
