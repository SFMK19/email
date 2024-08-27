package com.surf0335.AI_Recommendation_System.controller.old_controller;

public class TrainerNotFoundException extends RuntimeException{
    public TrainerNotFoundException(Long id) {
        super("Trainer not found with id: " + id);
    }
}
