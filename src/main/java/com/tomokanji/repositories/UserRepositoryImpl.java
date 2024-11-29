package com.tomokanji.repositories;

import com.tomokanji.model.Kanji;
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
    public boolean isUserExist(String username, String password) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
        return count != null && count > 0;
    }

    @Override
    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password, premium, session_cookie) VALUES (?, ?, 0, NULL)";
        return jdbcTemplate.update(sql, username, password) == 1;
    }

    @Override
    public boolean isUserPremium(int userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ? AND premium = 1";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    @Override
    public List<Integer> findMasteredWordIdsByUserId(int userId) {
        String sql = "SELECT word_id FROM user_mastered_words WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, userId);
    }

    @Override
    public List<Word> findMasteredWordsByUserId(int userId) {
        List<Integer> wordIds = findMasteredWordIdsByUserId(userId);
        return wordRepository.findWordsByIds(wordIds);
    }

    @Override
    public List<Integer> findMasteredKanjiIdsByUserId(int userId) {
        String sql = "SELECT kanji_id FROM user_mastered_kanji WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, userId);
    }

    @Override
    public List<Kanji> findMasteredKanjisByUserId(int userId) {
        List<Integer> kanjiIds = findMasteredKanjiIdsByUserId(userId);
        System.out.println("kanjiIds: " + kanjiIds);
        return kanjiRepository.findKanjisByIds(kanjiIds);
    }

    @Override
    public boolean masterWord(int userId, int wordId) {
        String sql = "INSERT INTO user_mastered_words (user_id, word_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, userId, wordId) == 1;
    }

    @Override
    public boolean masterKanji(int userId, int kanjiId) {
        String sql = "INSERT INTO user_mastered_kanji (user_id, kanji_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, userId, kanjiId) == 1;
    }

    @Override
    public boolean unmasterWord(int userId, int wordId) {
        String sql = "DELETE FROM user_mastered_words WHERE user_id = ? AND word_id = ?";
        return jdbcTemplate.update(sql, userId, wordId) == 1;
    }

    @Override
    public boolean unmasterKanji(int userId, int kanjiId) {
        String sql = "DELETE FROM user_mastered_kanji WHERE user_id = ? AND kanji_id = ?";
        return jdbcTemplate.update(sql, userId, kanjiId) == 1;
    }
}
