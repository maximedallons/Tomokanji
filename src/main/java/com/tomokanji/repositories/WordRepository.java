package com.tomokanji.repositories;

import com.tomokanji.model.Word;

import java.util.List;

public interface WordRepository {
    List<Word> getWords();

    List<Word> getWordsByLevel(int level);

    List<Word> getWordsByQuery(String query);

    List<Word> findWordsByIds(List<Integer> wordIds);
}
