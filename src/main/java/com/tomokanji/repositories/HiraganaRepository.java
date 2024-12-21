package com.tomokanji.repositories;

import com.tomokanji.model.Kana;

import java.util.List;

public interface HiraganaRepository {
    List<Kana> getHiraganas();

    List<Kana> getHiraganasByQuery(String query);

    List<Kana> findHiraganasByIds(List<Integer> kanjiIds);
}
