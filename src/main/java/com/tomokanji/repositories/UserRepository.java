package com.tomokanji.repositories;

import com.tomokanji.model.Word;

import java.util.List;

public interface UserRepository {
    boolean isUserExist(String username, String password);

    boolean isUserPremium(String userId);

    List<Integer> findMasteredWordIdsByUserId(String userId);
}
