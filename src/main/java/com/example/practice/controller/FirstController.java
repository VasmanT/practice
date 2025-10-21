package com.example.practice.controller;

import com.example.practice.service.FirstServiceImpl;
import com.example.practice.service.ServiceDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api", "/api/"})
public class FirstController {
    private final FirstServiceImpl firstService;


    public FirstController(FirstServiceImpl firstService) {
        this.firstService = firstService;
    }

    @GetMapping
    public ResponseEntity<String> getById(){

        String dto = firstService.getData();
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

}
