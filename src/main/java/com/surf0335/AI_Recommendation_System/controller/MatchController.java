package com.surf0335.AI_Recommendation_System.controller;

import com.surf0335.AI_Recommendation_System.services.MatchService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MatchController {
    @Autowired
    private MatchService matchService;

    @GetMapping("/match_student")
    public String matchStudent(Model model, HttpSession session) {
        return "match_student";
    }

    @GetMapping("/match_teacher")
    public String matchTeacher(Model model, HttpSession session) {
        return "match_teacher";
    }

}
