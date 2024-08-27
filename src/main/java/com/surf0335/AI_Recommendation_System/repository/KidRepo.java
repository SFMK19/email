package com.surf0335.AI_Recommendation_System.repository;
import org.springframework.stereotype.Repository;

import com.surf0335.AI_Recommendation_System.model.Kid;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface KidRepo extends JpaRepository<Kid,Integer>{

  

}


    

