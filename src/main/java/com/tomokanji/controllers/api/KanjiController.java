package com.tomokanji.controllers.api;

import com.tomokanji.model.Kanji;
import com.tomokanji.repositories.KanjiRepository;
import com.tomokanji.repositories.LogRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
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
        LogRepository.addLog("[GET] /api/kanjis/");
        return kanjiRepository.getKanjis();
    }

    @GetMapping("/level/{level}")
    public List<Kanji> getKanjisByLevel(@PathVariable("level") int level) {
        LogRepository.addLog("[GET] /api/kanjis/level/"+level);
        return kanjiRepository.getKanjisByLevel(level);
    }

    @GetMapping("/search")
    public List<Kanji> getKanjisByQuery(@RequestParam("query") String query) {
        LogRepository.addLog("[GET] /api/kanjis/search?query="+query);
        return kanjiRepository.getKanjisByQuery(query);
    }
}
