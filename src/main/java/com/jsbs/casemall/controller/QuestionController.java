package com.jsbs.casemall.controller;

import com.jsbs.casemall.entity.Question;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.QuestionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/qna")
public class QuestionController {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("question", new Question());

        return "qna/qnaWrite";
    }
    @GetMapping("/questions")
    public String showQuestionForm(Model model) {
        model.addAttribute("question", new Question());

        return "qna/questionForm"; //: questionForm은 질문 폼을 렌더링하는 Thymeleaf 템플릿
    }
    @PostMapping("/questions")
    public String submitQuestion(@ModelAttribute("question") Question question, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "qna/questionForm";
        }

        questionRepository.save(question); //: 저장소에 질문 저장

        return "redirect:/qna/questions";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionRepository.findOne(id));

        return "qna/show";
    }
    //수정&삭제 권한 체크 메서드
    private boolean hasPermission(HttpSession session, Question question){
        //로그인 여부 체크하는 로직 구현 필요.

        //본인여부 체크하는 로직 구현 필요.
        Users user = (Users) session.getAttribute("user");

//        if(!question.isSameWriter(user)){
//            throw new IllegalStateException("자신이 쓴 글만 수정, 삭제가 가능합니다.");
//        }

        return true;
    }
    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
        try{
            //현재 질문 조회
            Question question = questionRepository.findOne(id);
            //권한 체크
            hasPermission(session, question);
            //질문 수정화면으로 이동
            model.addAttribute("question", question);

            return "qna/updateForm";
        }catch(IllegalStateException e){
            //에러 메시지 전달
            model.addAttribute("errorMsg", e.getMessage());
            //로그인 페이지로 이동
            return "user/login";
        }
    }
    @PutMapping("/{id}")
    public String update(@PathVariable Long id, String title, String contents, Model model, HttpSession session) {
        try{
            //현재 질문 조회
            Question question = questionRepository.findOne(id);
            //권한 체크
            hasPermission(session, question);
            //업데이트 처리
            question.update(title, contents);
            questionRepository.save(question);

            return String.format("redirect:/questions/%d", id);
        }catch(IllegalStateException e){
            //에러 메시지 전달
            model.addAttribute("errorMsg", e.getMessage());
            //로그인 페이지 이동
            return "user/userLogin";
        }
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, HttpSession session, Model model){
        try{
            //현재 질문 조회
            Question question = questionRepository.findOne(id);
            //권한 체크
            hasPermission(session, question);
            //삭제 처리
            questionRepository.deleteById(id);

            return "redirect:/";
        }catch(IllegalStateException e){
            //에러 메시지 전달
            model.addAttribute("errorMsg", e.getMessage());
            //로그인 페이지로 이동
            return "user/login";
        }
    }
}
