package com.tomokanji.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kanji {
    private int id;
    private String character;
    private int level;
    private List<String> meanings;
    private List<String> onyomi;
    private List<String> kunyomi;
    private int strokes;
}
