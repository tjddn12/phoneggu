package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.CommentDto;
import com.jsbs.casemall.repository.CommentRepository;
import com.jsbs.casemall.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentController(CommentService commentService, CommentRepository commentRepository){
        this.commentService = commentService;
        this.commentRepository = commentRepository;
    }
    @GetMapping("/post/{refBoard}/{refPostNo}")
    public ResponseEntity<List<CommentDto>> getCommentsInPost(@PathVariable String refBoard,
                                                              @PathVariable int refPostNo){
        List<CommentDto> comments = commentService.getCommentsInPost(refBoard, refPostNo);

        return ResponseEntity.ok(comments);
    }
    @PostMapping
    public ResponseEntity<CommentDto> writeAComment(@RequestBody CommentDto commentDto){
        int commentId = commentService.writeAComment(commentDto);
        CommentDto createdComment = commentService.
                getCommentsInPost(commentDto.getRefBoard(), commentDto.getRefPostNo()).get(commentId);

        return ResponseEntity.ok(createdComment);
    }
}