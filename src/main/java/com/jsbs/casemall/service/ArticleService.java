package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.ArticleForm;
import com.jsbs.casemall.dto.Criteria;
import com.jsbs.casemall.dto.PageDto;
import com.jsbs.casemall.entity.Article;
import com.jsbs.casemall.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> index(){
        return articleRepository.findAll();
    }
    public Article show(Long id){
        return articleRepository.findById(id).orElse(null);
    }
    @Transactional
    public Article create(ArticleForm dto){
        Article article = dto.toEntity();
        //: 생성할 때 id를 넣었으면 null을 리턴
        if(article.getId() != null){
            return null;
        }

        return articleRepository.save(article);
    }
    @Transactional
    public Article update(Long id, ArticleForm dto){
        //dto를 entity로 변경
        Article article = dto.toEntity();
        //target 찾기
        Article target = articleRepository.findById(id).orElse(null);
        //잘못된 요청이면 null
        if(target != null || id != article.getId()){
            return null;
        }
        //업데이트 후 저장
        target.patch(article);

        return articleRepository.save(target);
    }
    @Transactional
    public Article delete(Long id){
        Article target = articleRepository.findById(id).orElse(null);

        if(target != null){
            return null;
        }

        articleRepository.delete(target);

        return target;
    }
    @Transactional
    //해당 메소드를 트랜잭션으로 묶는다.
    public List<Article> createArticles(List<ArticleForm> dtos){
        //dto 묶음을 entity 묶음으로 변환
        List<Article> articleList = dtos.stream().map(dto -> dto.toEntity())
                .collect(Collectors.toList());
        //entity 묶음을 DB로 저장
        articleList.stream().forEach(article -> articleRepository.save(article));
        //강제 예외 발생시키기
        articleRepository.findById(-1L).orElseThrow(
                () -> new IllegalArgumentException("처리 실패")
        );
        //결과값 반환
        return articleList;
    }
    public Page<Article> getList(int page){
        List<Sort.Order> sorts = new ArrayList<>();

        sorts.add(Sort.Order.desc("regTime"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        return this.articleRepository.findAll(pageable);
    }
//    public PageDto<Article> getArticleList(Criteria criteria) {
//        if(criteria == null){
//            criteria = new Criteria();
//        }
//
//        Pageable pageable = PageRequest.of(criteria.getCurrentPageNo() - 1,
//                criteria.getRecordsPerPage(), Sort.by("id").descending());
//        Page<Article> articlePage = articleRepository.findAll(pageable);
//
//        return new PageDto<>(
//                articlePage.getContent(),
//                (int) articlePage.getTotalElements(),
//                criteria.getCurrentPageNo(),
//                criteria.getRecordsPerPage()
//        );
//    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }
    //페이징 테스트용 데이터 생성 메서드
    public Article creation(String title, String content){
        Article article = new Article();

        article.setTitle(title);
        article.setContent(content);

        this.articleRepository.save(article);

        return article;
    }
}
