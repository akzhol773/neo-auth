package com.neobis.neoauth.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Hidden
@RestController
public class Demo {
    @GetMapping("/home")
    public String getHome(){
        return "Hello from secured home page";
    }
}
