package com.hundanli.gulimall.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/11/1 15:23
 */
@RestController
public class GreetingController {
    @GetMapping("/greeting")
    public String greeting() {
        return "greeting";
    }
}
