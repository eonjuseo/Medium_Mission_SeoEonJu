package com.ll.medium.domain.post;

import com.ll.medium.domain.comment.CommentForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/post")
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    //글 목록 조회
    @GetMapping("/list")
    public String readList(Model model) {
        List<Post> postList = this.postService.readList();
        model.addAttribute("postList", postList);
        return "post_list";
    }

    //내 글 목록 조회
    //@GetMapping("/myList")

    //글 상세내용 조회
    @GetMapping(value = "/{id}")
    public String readPost(Model model, @PathVariable("id") Long id, CommentForm commentForm) {
        Post post = this.postService.readPost(id);
        model.addAttribute("Post", post);
        return "post_detail";
    }

    //글 작성
    @GetMapping("/write")
    public String createPost(PostForm postForm) {
        return "post_form";
    }

    @PostMapping("/write")
    public String createPost(@Valid PostForm postForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post_form";
        }
        this.postService.createPost(postForm.getSubject(), postForm.getContent());
        return "redirect:/post/list";
    }

    //글 수정
    //@GetMapping("/{id}/modify")
    //@PostMapping("/{id}/modify")

    //글 삭제
    //@DeleteMapping("/{id}/delete")

    //특정 회원의 글 모아보기
    //@GetMapping("/b/user1") //리스트
    //@GetMapping("/b/user1/3") //상세

}
