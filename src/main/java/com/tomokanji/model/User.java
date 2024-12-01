package com.tomokanji.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int user_id;
    private String login;
    @JsonIgnore
    private String password;
    private boolean premium;
    private String cookie;
}
