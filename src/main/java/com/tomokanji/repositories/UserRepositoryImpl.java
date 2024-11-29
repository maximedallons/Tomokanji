//package com.tomokanji.repositories;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class UserRepositoryImpl implements UserRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public boolean isUserExist(String username, String password) {
//        return jdbcTemplate.queryForObject(
//                "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?",
//                new Object[]{username, password},
//                Integer.class
//        ) > 0;
//    }
//
//    @Override
//    public boolean isUserPremium(String userId) {
//        return false;
//    }
//
//    @Override
//    public List<Integer> findMasteredWordIdsByUserId(String userId) {
//        return jdbcTemplate.query(
//                "SELECT word_id FROM user_mastered_words WHERE user_id = ?",
//                new Object[]{userId},
//                (rs, rowNum) -> rs.getInt("word_id")
//        );
//    }
//}
