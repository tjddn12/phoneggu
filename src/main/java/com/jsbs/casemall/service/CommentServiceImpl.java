package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.CommentDto;
import com.jsbs.casemall.entity.Comment;
import com.jsbs.casemall.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }
    @Override
    public List<CommentDto> getCommentsInPost(String refBoard, int refPostNo){
        //CommentRepository를 사용하여 게시글의 댓글을 조회하는 로직 구현
        List<Comment> comments = commentRepository.findCommentsByRefBoardAndRefPostNo(refBoard, refPostNo);
        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    //    @Override
//    public List<CommentDto> getNestedCommentsInPost(String refBoard, int refPostNo) {
//        List<Comment> comments = commentRepository.findNestedCommentsByRefBoardAndRefPostNo(refBoard, refPostNo);
//        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
//    }
    @Override
    public List<CommentDto> getNestedCommentsInPost(String refBoard, int refPostNo){
        //CommentRepository를 사용하여 게시글의 답글(대댓글)을 조회하는 로직 구현
        List<Comment> comments = commentRepository.findNestedCommentsByRefBoardAndRefPostNo(refBoard, refPostNo);

        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    @Override
    public int writeAComment(CommentDto commentDto){
        //CommentRepository를 사용하여 댓글을 작성하는 로직을 구현
        Comment comment = new Comment();
        //CommentDto에서 필요한 정보를 Comment 엔티티로 매핑하여 저장하는 로직을 구현
        comment.setMemberId(commentDto.getMemberId());
        comment.setRefBoard(commentDto.getRefBoard());
        comment.setRefPostNo(commentDto.getRefPostNo());
        comment.setCommentContent(commentDto.getCommentContent());
        comment.setCommentRegDate(commentDto.getCommentRegDate());
        comment.setCommentChgDate(commentDto.getCommentChgDate());
        comment.setCommentDelDate(commentDto.getCommentDelDate());
        comment.setCommentNestLevel(commentDto.getCommentNestLevel());
        comment.setCommentNestedTo(commentDto.getCommentNestedTo());

        commentRepository.save(comment);

        return comment.getCommentNo().intValue(); //: 새로 생성된 댓글의 번호 반환
    }
    @Override
    public int editAComment(int commentNo, String commentContent){
        //CommentRepository를 사용하여 댓글을 수정하는 로직을 구현
        return commentRepository.updateCommentContent(commentNo, commentContent);
    }
    @Override
    public int deleteAComment(int commentNo){
        //CommentRepository를 사용하여 댓글을 삭제하는 로직을 구현
        Comment comment = commentRepository.findById((long) commentNo).orElse(null);
        //: JPARepository에서 'Long' 타입 인자를 필요로 하므로 변환
        if(comment != null){
            commentRepository.delete(comment);

            return 1; //: 삭제 성공
        }else{
            return 0; //: 삭제할 댓글이 없음
        }
    }
    @Override
    public int getTotalCommentNumber(String refBoard, int refPostNo){
        //CommentRepository를 사용하여 게시글에 등록된 총 댓글 수를 조회하는 로직을 구현
        return commentRepository.countByRefBoardAndRefPostNo(refBoard, refPostNo);
    }
    @Override
    public Map<String, String> checkInquiryPostWriter(int refPostNo){
        //CommentRepository를 사용하여 특정 게시글의 문의글 작성자 정보를 조회하는 로직을 구현
        return commentRepository.findInquiryPostWriter(refPostNo);
    }
    @Override
    public CommentDto checkParentComment(int commentNestedTo){
        //CommentRepository를 사용하여 부모 댓글 정보를 조회하는 로직을 구현
        Comment parentComment = commentRepository.findParentComment(commentNestedTo);

        if(parentComment != null){
            return convertToDto(parentComment);
        }else{
            return null; //: 부모 댓글이 없을 경우
        }
    }

    private CommentDto convertToDto(Comment comment){
        CommentDto commentDto = new CommentDto();

        commentDto.setCommentNo(comment.getCommentNo().intValue());
        commentDto.setMemberId(comment.getMemberId());
        commentDto.setRefBoard(comment.getRefBoard());
        commentDto.setRefPostNo(comment.getRefPostNo());
        commentDto.setCommentContent(comment.getCommentContent());
        commentDto.setCommentRegDate(comment.getCommentRegDate());
        commentDto.setCommentChgDate(comment.getCommentChgDate());
        commentDto.setCommentDelDate(comment.getCommentDelDate());
        commentDto.setCommentNestLevel(comment.getCommentNestLevel());
        commentDto.setCommentNestedTo(comment.getCommentNestedTo());
        commentDto.setMemberId(comment.getMemberId());

        return commentDto;
    }
}