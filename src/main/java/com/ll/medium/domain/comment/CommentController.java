package com.ll.medium.domain.comment;

import com.ll.medium.domain.member.Member;
import com.ll.medium.domain.member.MemberService;
import com.ll.medium.domain.post.Post;
import com.ll.medium.domain.post.PostService;
import com.ll.medium.domain.comment.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {

    private final PostService postService;
    private final CommentService commentService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/write")
    public String createComment(Model model, @PathVariable("id") Long id,
                                @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {
        Post post = this.postService.readPost(id);
        Member member = this.memberService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "post_detail";
        }
        this.commentService.createComment(post, commentForm.getContent(), member);
        return String.format("redirect:/post/%s", id);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String answerModify(CommentForm commentForm, @PathVariable("id") Long id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentForm.setContent(comment.getContent());
        return "comment_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String answerModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                               @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.commentService.updateComment(comment, commentForm.getContent());
        return String.format("redirect:/post/%s", comment.getPost().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete")
    public String answerDelete(Principal principal, @PathVariable("id") Long id) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.deleteComment(comment);
        return String.format("redirect:/post/%s", comment.getPost().getId());
    }
}
