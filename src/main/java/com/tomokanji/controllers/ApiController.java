package com.tomokanji.controllers;

import com.tomokanji.model.Kanji;
import com.tomokanji.model.Word;
import com.tomokanji.repositories.KanjiRepository;
import com.tomokanji.repositories.WordRepository;
import com.tomokanji.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final WordRepository wordRepository;
    private final KanjiRepository kanjiRepository;
//    private final UserRepository userRepository;

    public ApiController(WordRepository wordRepository, KanjiRepository kanjiRepository) {
        this.wordRepository = wordRepository;
        this.kanjiRepository = kanjiRepository;
//        this.userRepository = userRepository;
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

    @GetMapping("/kanjis")
    public List<Kanji> getKanjis() {
        return kanjiRepository.getKanjis();
    }

    @GetMapping("/kanjis/level/{level}")
    public List<Kanji> getKanjisByLevel(@PathVariable("level") int level) {
        System.out.println("level: " + level);
        return kanjiRepository.getKanjisByLevel(level);
    }

    @GetMapping("/kanjis/search")
    public List<Kanji> getKanjisByQuery(@RequestParam("query") String query) {
        return kanjiRepository.getKanjisByQuery(query);
    }

//    @GetMapping("/user/{user_id}/{password}")
//    public boolean isUSerExist(@PathVariable("user_id") String username, @PathVariable("password") String password) {
//        return userRepository.isUserExist(username, password);
//    }
//
//    @GetMapping("/user/{user_id}/premium")
//    public boolean isUserPremium(@PathVariable("user_id") String userId) {
//        return userRepository.isUserPremium(userId);
//    }
//
//    @GetMapping("/user/{user_id}/words")
//    public List<Word> getMasteredWords(@PathVariable("user_id") String user_id) {
//        return null;
//    }
}