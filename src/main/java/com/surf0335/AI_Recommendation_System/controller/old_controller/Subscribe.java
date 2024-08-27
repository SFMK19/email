package com.surf0335.AI_Recommendation_System.controller.old_controller;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;

// import model.com.surf0335.AI_Recommendation_System.User;
// import repository.com.surf0335.AI_Recommendation_System.UserRepo;

@Controller
public class Subscribe {
    @GetMapping("/subscribe_page")
    public String getSubscribePage(Model m){
        return "subscribe";
    }
       
}
