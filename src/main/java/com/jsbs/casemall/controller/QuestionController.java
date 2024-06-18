package com.jsbs.casemall.controller;

import com.jsbs.casemall.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
//        model.addAttribute("question", questionRepository.findOne(id));

        return "/qna/show";
    }
}
