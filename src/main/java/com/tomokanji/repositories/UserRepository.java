package com.tomokanji.repositories;

import com.tomokanji.model.User;
import java.util.List;

public interface UserRepository {
    User isUserExist(String username, String password);

    User registerUser(String username, String password);

    boolean isUserPremium(int userId);

    List<Integer> findMasteredWordIdsByUserId(int userId);

    List<Integer> findMasteredKanjiIdsByUserId(int userId);

    List<Integer> findMasteredHiraganaIdsByUserId(int userId);

    List<Integer> findMasteredKatakanaIdsByUserId(int userId);

    boolean masterWord(int userId, int wordId);

    boolean masterKanji(int userId, int kanjiId);

    boolean masterHiragana(int userId, int kanaId);

    boolean masterKatakana(int userId, int kanaId);

    boolean unmasterWord(int userId, int wordId);

    boolean unmasterKanji(int userId, int kanjiId);

    boolean unmasterHiragana(int userId, int kanaId);

    boolean unmasterKatakana(int userId, int kanaId);
}
