package com.tomokanji.controllers.api;

import com.tomokanji.model.Kana;
import com.tomokanji.repositories.HiraganaRepository;
import com.tomokanji.repositories.KatakanaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kanas")
public class KanaController {

    private final HiraganaRepository hiraganaRepository;
    private final KatakanaRepository katakanaRepository;

    public KanaController(HiraganaRepository hiraganaRepository, KatakanaRepository katakanaRepository) {
        this.hiraganaRepository = hiraganaRepository;
        this.katakanaRepository = katakanaRepository;
    }

    @GetMapping("/hiraganas")
    public List<Kana> getHiraganas() {
        return hiraganaRepository.getHiraganas();
    }

    @GetMapping("/katakanas")
    public List<Kana> getKatakanas() {
        return katakanaRepository.getKatakanas();
    }
}
