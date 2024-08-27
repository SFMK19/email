package com.surf0335.AI_Recommendation_System.repository;
import org.springframework.stereotype.Repository;

import com.surf0335.AI_Recommendation_System.model.Manager;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ManagerRepo extends JpaRepository<Manager,Integer>{
    Manager findByName(String name);
}
