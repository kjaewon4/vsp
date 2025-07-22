package com.bu.startup.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bu.startup.dto.UserRegisterDto;
import com.bu.startup.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserRegisterDto());
        return "register";
    }

    // POST: DTO로 한 번에 바인딩 + 유효성 검사
    @PostMapping("/register")
    public String register(
            @ModelAttribute("user") @Valid UserRegisterDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            // 유효성 오류가 있으면 다시 폼으로
            return "register";
        }

        // DTO에서 값 꺼내서 서비스 호출
        userService.register(
            dto.getUsername(),
            dto.getPassword(),
            dto.getNickname(),
            dto.getEmail()
        );
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "home";
    }
}
