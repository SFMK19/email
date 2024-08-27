package com.surf0335.AI_Recommendation_System.repository;

import com.surf0335.AI_Recommendation_System.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainerRepo extends JpaRepository<Trainer, Long> {

    List<Trainer> findByName(String name);
    List<Trainer> findByCourse(String Course);

    

}
