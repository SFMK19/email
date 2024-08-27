package com.surf0335.AI_Recommendation_System.repository;

import com.surf0335.AI_Recommendation_System.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByTrainerId(Long trainerId);
    List<Appointment> findByTrainerIdAndDateAndTime(int trainerId,String date, String time);
    List<Appointment> findByUserId(int userId);
}

