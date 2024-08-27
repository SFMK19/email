package com.surf0335.AI_Recommendation_System.repository;
import org.springframework.stereotype.Repository;

// import com.cpt202.FitnessTrainerAppointmentSystem.controller.Appointment;
import com.surf0335.AI_Recommendation_System.model.Plan;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface PlanRepo extends JpaRepository<Plan,Integer>{

}
