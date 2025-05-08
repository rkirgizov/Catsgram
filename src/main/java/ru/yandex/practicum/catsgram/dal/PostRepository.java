package ru.yandex.practicum.catsgram.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.catsgram.model.Post;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository extends BaseRepository<Post> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM posts";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM posts WHERE id = ?";
    private static final String FIND_BY_USERID_QUERY = "SELECT * FROM posts WHERE author_id = ?";
    private static final String FIND_BY_POSTDATE_QUERY = "SELECT * FROM posts WHERE post_date = ?";
    private static final String INSERT_QUERY = "INSERT INTO posts(author_id, description, post_date)" +
            "VALUES (?, ?, ?) returning id";
    private static final String UPDATE_QUERY = "UPDATE posts SET description = ? WHERE id = ?";

    public PostRepository(JdbcTemplate jdbc, RowMapper<Post> mapper) {
        super(jdbc, mapper);
    }

    public List<Post> findAll(String sort, Integer from, Integer size) {
        String queryWithSortOffsetLimit = FIND_ALL_QUERY + " ORDER BY post_date " + sort + " OFFSET ? LIMIT ?";
        return findMany(queryWithSortOffsetLimit, from, size);
    }

    public Optional<Post> findById(long postId) {
        return findOne(FIND_BY_ID_QUERY, postId);
    }

    // Поиск по автору
    public List<Post> findByUserId(long userId) {
        return findManyById(FIND_BY_USERID_QUERY, userId);
    }

    public List<Post> findByPostDate(Instant postDate) {
        return findMany(FIND_BY_POSTDATE_QUERY, postDate);
    }

    public Post save(Post post) {
        long id = insert(
                INSERT_QUERY,
                post.getAuthorId(),
                post.getDescription(),
                Timestamp.from(post.getPostDate())
        );
        post.setId(id);
        return post;
    }

    public Post update(Post post) {
        update(
                UPDATE_QUERY,
                post.getDescription(),
                post.getId()
        );
        return post;
    }
}
