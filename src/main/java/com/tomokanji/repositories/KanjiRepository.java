package com.tomokanji.repositories;

import com.tomokanji.model.Kanji;

import java.util.List;

public interface KanjiRepository {
    List<Kanji> getKanjis();

    List<Kanji> getKanjisByLevel(int level);

    List<Kanji> getKanjisByQuery(String query);
}
