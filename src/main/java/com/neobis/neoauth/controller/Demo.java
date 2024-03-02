package com.neobis.neoauth.controller;

import com.neobis.neoauth.dtos.JwtRequestDto;
import com.neobis.neoauth.dtos.JwtResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo {

    @GetMapping("/get")
    public ResponseEntity<String> login(){
        return  ResponseEntity.ok().body("Hello from secured endpoint");

    }
}
