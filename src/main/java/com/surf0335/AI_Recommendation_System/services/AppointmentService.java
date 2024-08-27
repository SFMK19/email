package com.surf0335.AI_Recommendation_System.services;

import com.surf0335.AI_Recommendation_System.model.Appointment;
import com.surf0335.AI_Recommendation_System.repository.AppointmentRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepo appointmentRepo;

    public Appointment createOrUpdateAppointment(Appointment appointment) {
        return appointmentRepo.save(appointment);
    }

    public List<Appointment> listAppointments() {
        return appointmentRepo.findAll();
    }

    public Optional<Appointment> getAppointmentById(int id) {
        return appointmentRepo.findById(id);
    }

    public void deleteAppointment(int id) {
        Optional<Appointment> optionalAppointment = appointmentRepo.findById(id);
        appointmentRepo.deleteById(id);
    }

    public List<Appointment> getAppointmentsByTrainerId(Long trainerId) {
        return appointmentRepo.findByTrainerId(trainerId);
    }

}
