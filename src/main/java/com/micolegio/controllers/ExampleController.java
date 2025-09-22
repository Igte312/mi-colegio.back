package com.micolegio.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("example")
public class ExampleController {

    @GetMapping("/hello")
    public Map<String, String> helloWorld() {

        return Map.of("message","Hola desde API");
    }
}
