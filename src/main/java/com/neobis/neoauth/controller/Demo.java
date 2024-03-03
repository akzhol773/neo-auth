package com.neobis.neoauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo {
    @GetMapping("/home")
    public String getHome(){
        return "Hello from secured home page";
    }
}
