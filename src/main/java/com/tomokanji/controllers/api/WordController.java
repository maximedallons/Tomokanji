package com.tomokanji.controllers.api;

import com.tomokanji.model.Word;
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

    @GetMapping("/words")
    public List<Word> getWords() {
        return wordRepository.getWords();
    }

    @GetMapping("/words/level/{level}")
    public List<Word> getWordsByLevel(@PathVariable("level") int level) {
        return wordRepository.getWordsByLevel(level);
    }

    @GetMapping("/words/search")
    public List<Word> getWordsByQuery(@RequestParam("query") String query) {
        return wordRepository.getWordsByQuery(query);
    }
}
