package com.tomokanji.repositories;

import com.tomokanji.model.Kanji;
import com.tomokanji.model.Word;

import java.util.List;

public interface UserRepository {
    boolean isUserExist(String username, String password);

    boolean registerUser(String username, String password);

    boolean isUserPremium(int userId);

    List<Integer> findMasteredWordIdsByUserId(int userId);

    List<Word> findMasteredWordsByUserId(int userId);

    List<Integer> findMasteredKanjiIdsByUserId(int userId);

    List<Kanji> findMasteredKanjisByUserId(int userId);

    boolean masterWord(int userId, int wordId);

    boolean masterKanji(int userId, int kanjiId);

    boolean unmasterWord(int userId, int wordId);

    boolean unmasterKanji(int userId, int kanjiId);
}
