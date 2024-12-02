package com.tomokanji.repositories;

import com.tomokanji.model.Kanji;
import com.tomokanji.model.User;
import com.tomokanji.model.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final WordRepository wordRepository;

    private final KanjiRepository kanjiRepository;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate, WordRepository wordRepository, KanjiRepository kanjiRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.wordRepository = wordRepository;
        this.kanjiRepository = kanjiRepository;
    }

    @Override
    public User isUserExist(String login, String password) {
        String sql = "SELECT COUNT(*) FROM users WHERE login = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, login, password);
        if(count != null && count > 0) {
            return findUserByLogin(login);
        }
        return null;
    }

    @Override
    public User registerUser(String login, String password) {
        String sql = "INSERT INTO users (login, password, premium, cookie) VALUES (?, ?, 0, NULL)";
        if(jdbcTemplate.update(sql, login, password) == 1) {
            return findUserByLogin(login);
        }
        return null;
    }

    private User findUserByLogin(String login) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, User.class, login);
    }

    @Override
    public boolean isUserPremium(int userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ? AND premium = 1";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    @Override
    public List<Integer> findMasteredWordIdsByUserId(int userId) {
        String sql = "SELECT word_id FROM user_word_mastered WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, userId);
    }

    @Override
    public List<Integer> findMasteredKanjiIdsByUserId(int userId) {
        String sql = "SELECT kanji_id FROM user_kanji_mastered WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, userId);
    }

    @Override
    public boolean masterWord(int userId, int wordId) {
        String sql = "INSERT INTO user_word_mastered (user_id, word_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, userId, wordId) == 1;
    }

    @Override
    public boolean unmasterWord(int userId, int wordId) {
        String sql = "DELETE FROM user_word_mastered WHERE user_id = ? AND word_id = ?";
        return jdbcTemplate.update(sql, userId, wordId) == 1;
    }

    @Override
    public boolean masterKanji(int userId, int kanjiId) {
        String sql = "INSERT INTO user_kanji_mastered (user_id, kanji_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, userId, kanjiId) == 1;
    }

    @Override
    public boolean unmasterKanji(int userId, int kanjiId) {
        String sql = "DELETE FROM user_kanji_mastered WHERE user_id = ? AND kanji_id = ?";
        return jdbcTemplate.update(sql, userId, kanjiId) == 1;
    }
}
