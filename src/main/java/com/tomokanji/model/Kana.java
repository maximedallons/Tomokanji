package com.tomokanji.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kana {
    private int id;
    private String kana;
    private String romaji;
}
