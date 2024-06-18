package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Question;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    default Question findOne(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }
}