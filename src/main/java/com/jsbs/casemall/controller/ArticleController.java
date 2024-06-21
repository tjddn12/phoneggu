package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.ArticleForm;
import com.jsbs.casemall.dto.CommentDto;
import com.jsbs.casemall.dto.Criteria;
import com.jsbs.casemall.dto.PageDto;
import com.jsbs.casemall.entity.Article;
import com.jsbs.casemall.repository.ArticleRepository;
import com.jsbs.casemall.service.ArticleService;
import com.jsbs.casemall.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Slf4j
@RequestMapping("/qnas")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleRepository articleRepository;
    private final CommentService commentService;
    private final ArticleService articleService;

    @GetMapping("/new")
    public String newArticleForm(){
        return "qna/new";
    }

    @PostMapping("/create")
    public String createArticle(ArticleForm form, Principal principal){
        String userId= principal.getName();

        long id = articleService.createQnaFromForm(userId,form);

        return "redirect:/qnas/" + id;
    }
    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model, Principal principal) {
        Article articleEntity = articleRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재 하지 않는 게시글입니다"));
        List<CommentDto> commentsDtos = commentService.comments(id);
        // 해당글을의 작성자 확인
        boolean writer = false;
        if (principal != null) {
            writer = articleEntity.getUser().getUserId().equals(principal.getName());
        }


        model.addAttribute("article", articleEntity);
        model.addAttribute("writer",writer);
        model.addAttribute("comments", commentsDtos);

//        log.info(commentsDtos.toString());
        //보여줄 페이지 설정
        return "qna/show";
    }
    @GetMapping
    public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page){
        Page<Article> paging = this.articleService.getList(page);
        Page<ArticleForm> articleDtos = paging.map(article -> {
            return new ArticleForm(
                    article.getId(),
                    article.getTitle(),
                    article.getContent(),
                    article.getUser().getUserId(),
                    commentService.comments(article.getId()).size(),
                    article.getRegTime()
            );
        });

        model.addAttribute("paging", articleDtos);

        log.info(paging.toString());
        List<Article> articleEntityList = articleRepository.findAll();

//        List<ArticleForm> articleForms = articleService.getAllArticles().stream()
//                .map(article -> {ArticleForm articleForm = new ArticleForm(
//                        article.getId(),
//                        article.getTitle(),
//                        article.getContent(),
//                        commentService.comments(article.getId()).size(),
//                        article.getRegTime()
//                ); //: 유저 id도 추가하여야 함.
//                    return articleForm;
//                }).collect(Collectors.toList());
//
//        model.addAttribute("articleList", articleForms);
//        model.addAttribute("articleList", articleEntityList);

        return "qna/list";
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        //수정할 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
        //모델에 데이터 등록
        model.addAttribute("article", articleEntity);

        return "qna/edit";
    }
    @PostMapping("/update")
    public String update(ArticleForm form,Principal principal){
        String userId= principal.getName();
        //dto -> entity


        return "redirect:/qnas/" + articleService.updateQnaFromForm(userId,form);
    }
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        //삭제 대상 가져옴
        Article target = articleRepository.findById(id).orElse(null);
        //대상 삭제
        if(target != null){
            articleRepository.delete(target);

            rttr.addFlashAttribute("msg", "삭제가 완료되었습니다.");
        }
        //결과 페이지로 리다이렉트
        return "redirect:/qnas/";
    }
}