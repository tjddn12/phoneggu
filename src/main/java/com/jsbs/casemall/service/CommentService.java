package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.CommentDto;
import com.jsbs.casemall.entity.Article;
import com.jsbs.casemall.entity.Comment;
import com.jsbs.casemall.repository.ArticleRepository;
import com.jsbs.casemall.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    //댓글이 달린 게시글도 가져와야하므로 ArticleRepository도 함께 가져오기
    public List<CommentDto> comments(Long articleId){
        //조회: 댓글 목록 조회(게시글 아이디를 통해 해당 게시글의 댓글 목록 조회)
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        comments.size();
        //변환: 엔티티 -> dto
        List<CommentDto> dtos = new ArrayList<CommentDto>();
        //비어있는 dtos에 댓글들을 변환해서 add
        for(int i = 0; i < comments.size(); i++){
            //comments 값을 하나하나 꺼내서 넣기
            Comment c = comments.get(i);
            CommentDto dto = CommentDto.createCommentDto(c); //: dto 변환

            dtos.add(dto);
        }
        //dto 리스트 반환
        return dtos;
    }
    @Transactional
    public CommentDto create(Long articleId, CommentDto dto){
        //게시글 조회 및 예외 발생
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("답변 생성 실패! 대상 질문이 없습니다."));
        //댓글 엔티티 생성
        Comment comment = Comment.createComment(dto, article);
        // 댓글 엔티티를 DB로 저장
        Comment created = commentRepository.save(comment);
        // DTO로 변환하여 반환
        //return CommentDto.createCommentDto(created);
        CommentDto createDto= CommentDto.createCommentDto(created);
        //log.info("반환값 =>{}",createDto);
        return createDto;
    }
    // 댓글 조회 및 예외 발생
    public CommentDto update(Long id, CommentDto dto) {
        Comment target = commentRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("답변 수정 실패! 대상 질문이 없습니다."));
        //댓글 수정
        target.patch(dto);

        // 댓글 DB로 갱신
        Comment updated = commentRepository.save(target);

        // 댓글 엔티티를 DTO 로 변환 및  반환
        return  CommentDto.createCommentDto(updated);
    }
    public CommentDto delete(Long id) {
        Comment target = commentRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("답변 삭제 실패! 대상 답변이 없습니다."));
        //댓글을 DB에서 삭제
        commentRepository.delete(target);

        return CommentDto.createCommentDto(target);
    }
}
