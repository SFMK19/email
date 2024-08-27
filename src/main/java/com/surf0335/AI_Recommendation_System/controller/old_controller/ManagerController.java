package com.surf0335.AI_Recommendation_System.controller.old_controller;
import com.surf0335.AI_Recommendation_System.repository.ManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.surf0335.AI_Recommendation_System.model.Manager;
import com.surf0335.AI_Recommendation_System.model.User;

@Controller
public class ManagerController {
    @Autowired
    private ManagerRepo managerRepo;

    @GetMapping("/manager_page")
    public String getHomePage(Model m){
        return "manager";
    }

    @PostMapping("/admin_log_in")
    public String logIn(@RequestParam String name, @RequestParam String password, Model m) {
        Manager manager = managerRepo.findByName(name);
        if (manager != null && password.equals(manager.getPassword())) {
            return "redirect:/manager_page";
        } else {
            m.addAttribute("manager", new Manager());
            m.addAttribute("user", new User());
            m.addAttribute("loginerror", "Invalid manager account. Please confirm your authentication.");
            return "logIn"; 
        }
    }

}
