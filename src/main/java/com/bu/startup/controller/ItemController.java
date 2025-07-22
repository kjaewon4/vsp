package com.bu.startup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {

    @GetMapping("/items")
    public String items(Model model, Principal principal) {
        return "items";
    }
}
