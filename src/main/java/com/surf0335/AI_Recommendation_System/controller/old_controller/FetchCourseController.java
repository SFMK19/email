package com.surf0335.AI_Recommendation_System.controller.old_controller;

import java.util.List;

import com.surf0335.AI_Recommendation_System.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import com.surf0335.AI_Recommendation_System.model.Course;


@RestController
public class FetchCourseController {
    @Autowired
    private CourseRepository courseRepository;
    @GetMapping("/courses")
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }
}
