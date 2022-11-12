package ru.job4j.accidents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegController {
    @GetMapping("/reg")
    public String regPage() {
        return "reg";
    }
}