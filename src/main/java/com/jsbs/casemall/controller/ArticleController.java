package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.ArticleForm;
import com.jsbs.casemall.dto.CommentDto;
import com.jsbs.casemall.dto.Criteria;
import com.jsbs.casemall.dto.PageDto;
import com.jsbs.casemall.entity.Article;
import com.jsbs.casemall.repository.ArticleRepository;
import com.jsbs.casemall.service.ArticleService;
import com.jsbs.casemall.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Slf4j
@RequestMapping("/qnas")
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleService articleService;

    @GetMapping("/new")
    public String newArticleForm(){
        return "qna/new";
    }
    @PostMapping("/create")
    public String createArticle(ArticleForm form){
        //dto -> entity 변환
        Article article = form.toEntity();
        //Repository에게 entity를 db 안에 저장하게 함
        Article saved = articleRepository.save(article);

        return "redirect:/qnas/" + saved.getId();
    }
    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model){
        Article articleEntity = articleRepository.findById(id).orElse(null);
        List<CommentDto> commentsDtos = commentService.comments(id);

        model.addAttribute("article", articleEntity);
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
    public String update(ArticleForm form){
        //dto -> entity
        Article articleEntity = form.toEntity();
        //entity DB 저장
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        //기존 데이터가 있으면 값 갱신
        if(target != null){
            articleRepository.save(articleEntity);
        }
        //수정 결과 페이지 리다이렉트
        return "redirect:/qnas/" + articleEntity.getId();
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