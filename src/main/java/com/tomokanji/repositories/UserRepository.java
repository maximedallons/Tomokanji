package com.tomokanji.repositories;

import com.tomokanji.model.Kanji;
import com.tomokanji.model.User;
import com.tomokanji.model.Word;

import java.util.List;

public interface UserRepository {
    User isUserExist(String username, String password);

    User registerUser(String username, String password);

    boolean isUserPremium(int userId);

    List<Integer> findMasteredWordIdsByUserId(int userId);

    List<Integer> findMasteredKanjiIdsByUserId(int userId);

    boolean masterWord(int userId, int wordId);

    boolean masterKanji(int userId, int kanjiId);

    boolean unmasterWord(int userId, int wordId);

    boolean unmasterKanji(int userId, int kanjiId);
}
