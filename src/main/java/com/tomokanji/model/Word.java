package com.tomokanji.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Word {
    private String id;
    private List<KanjiInfo> kanji;
    private List<KanaInfo> kana;
    private List<SenseInfo> sense;
    private Integer level;
}