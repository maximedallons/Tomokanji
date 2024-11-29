package com.tomokanji.controllers.api;

import com.tomokanji.model.Kanji;
import com.tomokanji.model.Word;
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
    public boolean isUserExist(@PathVariable("username") String username, @PathVariable("password") String password) {
        return userRepository.isUserExist(username, password);
    }

    @PostMapping("/{username}/{password}")
    public boolean register(@PathVariable("username") String username, @PathVariable("password") String password) {
        return userRepository.registerUser(username, password);
    }

    @GetMapping("/{user_id}/premium")
    public boolean isUserPremium(@PathVariable("user_id") int userId) {
        return userRepository.isUserPremium(userId);
    }

    @GetMapping("/{user_id}/words")
    public List<Word> getMasteredWords(@PathVariable("user_id") int user_id) {
        return userRepository.findMasteredWordsByUserId(user_id);
    }

    @GetMapping("/{user_id}/kanjis")
    public List<Kanji> getMasteredKanjis(@PathVariable("user_id") int user_id) {
        return userRepository.findMasteredKanjisByUserId(user_id);
    }
}
