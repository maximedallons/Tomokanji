package com.tomokanji.controllers;

import com.tomokanji.model.Entry;
import com.tomokanji.service.WordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    //Search endpoint
    @GetMapping("/search")
    public List<Entry> searchWords(String query, int level, boolean isCommon) {
        return wordService.searchWords(query, level, isCommon);
    }

    @GetMapping("/flashcards/level/{level}")
    public List<Entry> getFlashcards(@PathVariable int level) {
        return wordService.getFlashcardsByLevel(level);
    }

    @PostMapping("/flashcards/mastered/{entryId}")
    public void markFlashcardAsMastered(@PathVariable String entryId) {
        wordService.markAsMastered(entryId);
    }
}
