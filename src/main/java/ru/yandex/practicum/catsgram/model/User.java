package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = "email")
public class User {
    /*Класс User — модель, описывающая пользователей социальной сети. У неё следующие поля данных:
    Long id — уникальный идентификатор пользователя,
    String username — имя пользователя,
    String email — электронная почта пользователя,
    String password — пароль пользователя,
    Instant registrationDate — дата и время регистрации.*/
    private long id;
    private String username;
    private String email;
    private String password;
    private Instant registrationDate;
}
