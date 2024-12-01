package com.tomokanji.controllers.api;

import com.tomokanji.repositories.LogRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogController {

    @RequestMapping("/api/logs")
    public List<String> getLogs() {
        return LogRepository.getLogs();
    }
}
