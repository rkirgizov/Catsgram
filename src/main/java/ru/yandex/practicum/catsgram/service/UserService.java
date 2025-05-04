package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    //В класс UserService нужно добавить метод, который находит (и возвращает) пользователя по id: (например, с названием findUserById).
    // Метод должен возвращать Optional с пользователем, если он найден, и пустой Optional, если пользователь не найден.
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public User create(@RequestBody User user) {

        // проверяем выполнение необходимых условий
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (users.values()
                .stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        // формируем дополнительные данные
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        // сохраняем новую публикацию в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    public User update(@RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
                throw new ConditionsNotMetException("Имейл должен быть указан");
            }

            if (users.values()
                    .stream()
                    .anyMatch(u -> u.getEmail().equals(newUser.getEmail()))) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }

            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            if ((newUser.getUsername() != null)
                    && (!newUser.getUsername().isBlank())
                    && (!newUser.getUsername().isEmpty())) {
                oldUser.setUsername(newUser.getUsername());
            }

            if ((newUser.getPassword() != null)
                    && (!newUser.getPassword().isBlank())
                    && (!newUser.getPassword().isEmpty())) {
                oldUser.setPassword(newUser.getPassword());
            }

            if ((newUser.getEmail() != null)
                    && (!newUser.getEmail().isBlank())
                    && (!newUser.getEmail().isEmpty())) {
                oldUser.setEmail(newUser.getEmail());
            }

            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
