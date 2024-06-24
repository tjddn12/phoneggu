package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.ArticleForm;
import com.jsbs.casemall.entity.Article;
import com.jsbs.casemall.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ArticleApiController {
    @Autowired
    private ArticleService articleService;
    //전체 게시글 불러오기
    @GetMapping("/api/qnas")
    public List<Article> index(){
        return articleService.index();
    }
    //게시글 하나 불러오기
    @GetMapping("/api/qnas/{id}")
    public Article show(@PathVariable Long id){
        return articleService.show(id);
    }
    @PostMapping("/api/qnas")
    public ResponseEntity<Article> create(@RequestBody ArticleForm dto){
        Article created = articleService.create(dto);
        //잘 생성된 경우 GOOD : 아닐 경우 BAD
        return (created != null) ? ResponseEntity.status(HttpStatus.OK).body(created) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PatchMapping("/api/qnas/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id, @RequestBody ArticleForm dto){
        Article updated = articleService.update(id, dto);

        return (updated != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @DeleteMapping("/api/qnas/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id){
        Article deleted = articleService.delete(id);

        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //트랜잭션
    @PostMapping("/api/transaction-test")
    public ResponseEntity<List<Article>> transactionTest(@RequestBody List<ArticleForm> dtos){
        List<Article> createsList = articleService.createArticles(dtos);

        return (createsList != null) ? ResponseEntity.status(HttpStatus.OK).body(createsList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
