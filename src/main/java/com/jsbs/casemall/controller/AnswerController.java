package com.jsbs.casemall.controller;

import com.jsbs.casemall.entity.Answer;
import com.jsbs.casemall.entity.Question;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.AnswerRepository;
import com.jsbs.casemall.repository.QuestionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    //답변하기
    @PostMapping
    public String create(@PathVariable Long questionId, String contents, HttpSession session) {
        //유저 아이디 받아오는 로직 구현 필요.
        Users user = (Users) session.getAttribute("user");
        //Question 객체를 id로 받아오는 로직 구현 필요.
        Question question = questionRepository.findOne(questionId);
        //:
        Answer answer = new Answer(user, question, contents);

        answerRepository.save(answer);

        return "redirect:/questions/" + questionId;
    }

}
