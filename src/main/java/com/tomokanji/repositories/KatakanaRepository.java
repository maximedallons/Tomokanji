package com.tomokanji.repositories;

import com.tomokanji.model.Kana;

import java.util.List;

public interface KatakanaRepository {
    List<Kana> getKatakanas();

    List<Kana> getKatakanasByQuery(String query);

    List<Kana> findKatakanasByIds(List<Integer> kanjiIds);
}
