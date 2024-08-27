package com.surf0335.AI_Recommendation_System.services;

import com.surf0335.AI_Recommendation_System.controller.old_controller.TrainerNotFoundException;
import com.surf0335.AI_Recommendation_System.model.Trainer;
import com.surf0335.AI_Recommendation_System.repository.TrainerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    @Autowired
    private TrainerRepo trainerRepository;

    public Trainer createOrUpdateTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public List<Trainer> listTrainers() {
        return trainerRepository.findAll();
    }

    public Optional<Trainer> getTrainerById(Long id) {
        return trainerRepository.findById(id);
    }

    public void deleteTrainer(Long id) {
        Optional<Trainer> optionalTrainer = trainerRepository.findById(id);
        if (optionalTrainer.isPresent()) {
            trainerRepository.deleteById(id);
        } else {
            throw new TrainerNotFoundException(id);
        }
    }

    public List<Trainer> findTrainersByName(String name) {
        return trainerRepository.findByName(name);
    }
    

 

}
