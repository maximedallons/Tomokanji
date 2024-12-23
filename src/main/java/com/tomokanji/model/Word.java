package com.tomokanji.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Word {
    private int id;
    private List<KanjiInfo> kanjis;
    private List<KanaInfo> kanas;
    private List<String> translations;
    private List<Example> examples;
    private Integer level;

    public void removeUncommons() {
        kanjis.removeIf(kanji -> !kanji.isCommon());
        kanas.removeIf(kana -> !kana.isCommon());
    }

    public boolean hasOnlyUncommons() {
        return kanjis.stream().noneMatch(KanjiInfo::isCommon) && kanas.stream().noneMatch(KanaInfo::isCommon);
    }
}
