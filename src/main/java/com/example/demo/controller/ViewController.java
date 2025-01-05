package com.example.demo.controller;

import com.example.demo.service.MediaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@AllArgsConstructor
public class ViewController {
    private final MediaService mediaService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/browse")
    public String browse(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("pagedMedia", mediaService.getMediaByPage(page, 24));
        return "browse";
    }
}
