package com.NeuralSpring.api.repository;

import com.NeuralSpring.api.model.AnaliseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnaliseRepository extends JpaRepository<AnaliseReview, Long> {
}
