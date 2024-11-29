package com.tomokanji.controllers.api;

import com.tomokanji.model.Kanji;
import com.tomokanji.repositories.KanjiRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kanjis")
public class KanjiController {

    private final KanjiRepository kanjiRepository;

    public KanjiController(KanjiRepository kanjiRepository) {
        this.kanjiRepository = kanjiRepository;
    }

    @GetMapping("/")
    public List<Kanji> getKanjis() {
        return kanjiRepository.getKanjis();
    }

    @GetMapping("/level/{level}")
    public List<Kanji> getKanjisByLevel(@PathVariable("level") int level) {
        return kanjiRepository.getKanjisByLevel(level);
    }

    @GetMapping("/search")
    public List<Kanji> getKanjisByQuery(@RequestParam("query") String query) {
        return kanjiRepository.getKanjisByQuery(query);
    }
}
