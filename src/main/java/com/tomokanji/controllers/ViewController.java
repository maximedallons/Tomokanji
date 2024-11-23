package com.tomokanji.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("applicationTitle", "TomoKanji");
        return "index";
    }

    @GetMapping("/container")
    public String container(Model model) {
        return "/includes/container";
    }

    @GetMapping("/jlpt")
    public String jlpt(Model model) {
        return "/includes/jlpt";
    }
}
