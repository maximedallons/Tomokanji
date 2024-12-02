package com.tomokanji.controllers.api;

import com.tomokanji.model.Kanji;
import com.tomokanji.model.User;
import com.tomokanji.model.Word;
import com.tomokanji.repositories.LogRepository;
import com.tomokanji.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{username}/{password}")
    public User isUserExist(@PathVariable("username") String username, @PathVariable("password") String password) {
        LogRepository.addLog("[GET] /api/users/"+username+"/"+password);
        return userRepository.isUserExist(username, password);
    }

    @PostMapping("/{username}/{password}")
    public User register(@PathVariable("username") String username, @PathVariable("password") String password) {
        LogRepository.addLog("[POST] /api/users/"+username+"/"+password);
        return userRepository.registerUser(username, password);
    }

    @GetMapping("/{user_id}/premium")
    public boolean isUserPremium(@PathVariable("user_id") int userId) {
        LogRepository.addLog("[GET] /api/users/"+userId+"/premium");
        return userRepository.isUserPremium(userId);
    }

    @GetMapping("/{user_id}/words")
    public List<Integer> getMasteredWords(@PathVariable("user_id") int user_id) {
        LogRepository.addLog("[GET] /api/users/"+user_id+"/words");
        return userRepository.findMasteredWordIdsByUserId(user_id);
    }

    @PostMapping("/{user_id}/words/{word_id}")
    public boolean masterWord(@PathVariable("user_id") int user_id, @PathVariable("word_id") int word_id) {
        LogRepository.addLog("[POST] /api/users/"+user_id+"/words/"+word_id);
        return userRepository.masterWord(user_id, word_id);
    }

    @DeleteMapping("/{user_id}/words/{word_id}")
    public boolean unmasterWord(@PathVariable("user_id") int user_id, @PathVariable("word_id") int word_id) {
        LogRepository.addLog("[DELETE] /api/users/"+user_id+"/words/"+word_id);
        return userRepository.unmasterWord(user_id, word_id);
    }

    @GetMapping("/{user_id}/kanjis")
    public List<Integer> getMasteredKanjiIds(@PathVariable("user_id") int user_id) {
        LogRepository.addLog("[GET] /api/users/"+user_id+"/kanjis");
        return userRepository.findMasteredKanjiIdsByUserId(user_id);
    }

    @PostMapping("/{user_id}/kanjis/{kanji_id}")
    public boolean masterKanji(@PathVariable("user_id") int user_id, @PathVariable("kanji_id") int kanji_id) {
        LogRepository.addLog("[POST] /api/users/"+user_id+"/kanjis/"+kanji_id);
        return userRepository.masterKanji(user_id, kanji_id);
    }

    @DeleteMapping("/{user_id}/kanjis/{kanji_id}")
    public boolean unmasterKanji(@PathVariable("user_id") int user_id, @PathVariable("kanji_id") int kanji_id) {
        LogRepository.addLog("[DELETE] /api/users/"+user_id+"/kanjis/"+kanji_id);
        return userRepository.unmasterKanji(user_id, kanji_id);
    }
}
