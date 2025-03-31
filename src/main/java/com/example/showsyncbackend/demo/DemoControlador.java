package com.example.showsyncbackend.demo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api")
public class DemoControlador {
    @GetMapping("/hola-mundo")
    public String holaMundo() {
        return "Hola Mundo desde Spring Boot!";
    }
}
