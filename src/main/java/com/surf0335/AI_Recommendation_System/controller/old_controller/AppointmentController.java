package com.surf0335.AI_Recommendation_System.controller.old_controller;

import java.util.List;

import com.surf0335.AI_Recommendation_System.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.surf0335.AI_Recommendation_System.model.Course;

@Controller
public class AppointmentController {
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/appointment_page")
    public String get(Model m) {
        List<Course> courses = courseRepository.findAll();
        m.addAttribute("courses", courses);
        return "MakeAppointment";
    }

    @GetMapping("/managerViewAppointment")
    public String managerViewAppointment(Model m) {
        List<Course> courses = courseRepository.findAll();
        m.addAttribute("courses", courses);
        return "managerViewAppointment"; 
    }

}
