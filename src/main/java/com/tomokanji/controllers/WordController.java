package com.tomokanji.controllers;

import com.tomokanji.model.Entry;
import com.tomokanji.service.WordService;
import org.springframework.web.bind.annotation.GetMapping;
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

}