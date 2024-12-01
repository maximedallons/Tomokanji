package com.tomokanji.controllers.api;

import com.tomokanji.model.Word;
import com.tomokanji.repositories.LogRepository;
import com.tomokanji.repositories.WordRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/words")
public class WordController {

    private final WordRepository wordRepository;

    public WordController(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @GetMapping("/")
    public List<Word> getWords() {
        LogRepository.addLog("[GET] /api/words/");
        return wordRepository.getWords();
    }

    @GetMapping("/level/{level}")
    public List<Word> getWordsByLevel(@PathVariable("level") int level) {
        LogRepository.addLog("[GET] /api/words/level/"+level);
        return wordRepository.getWordsByLevel(level);
    }

    @GetMapping("/search")
    public List<Word> getWordsByQuery(@RequestParam("query") String query) {
        LogRepository.addLog("[GET] /api/words/search?query="+query);
        return wordRepository.getWordsByQuery(query);
    }
}
