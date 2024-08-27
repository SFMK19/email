package com.surf0335.AI_Recommendation_System.emailget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EmailController {

    @Autowired
    private MongoDBService mongoDBService;

    @GetMapping("/emails")
    public String viewEmails(Model model) {
        List<EmailData> emails = mongoDBService.findAllEmails();
        model.addAttribute("emails", emails);
        return "emails";
    }

}
