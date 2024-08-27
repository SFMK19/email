package com.surf0335.AI_Recommendation_System.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surf0335.AI_Recommendation_System.model.Course;



@Repository
public interface CourseRepository extends JpaRepository<Course,Integer>{
    
}
