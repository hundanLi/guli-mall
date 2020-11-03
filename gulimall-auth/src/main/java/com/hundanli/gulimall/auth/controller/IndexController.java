package com.hundanli.gulimall.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(value = {"/", "/index.html", "/login.html"})
    public String login(Model model) {
        return "login";
    }


    @GetMapping(value = {"/register", "//register.html"})
    public String register(Model model) {
        return "register";
    }
}
