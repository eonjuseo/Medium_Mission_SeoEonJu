package com.ll.medium.domain.post;

import com.ll.medium.domain.comment.CommentForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
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

    //내 글 목록
    @GetMapping("/myList")
    public String getUserPosts(Model model) {
        String loggedInUsername = "currentUser";
        List<Post> userPosts = postService.getPostsByUser(loggedInUsername);
        model.addAttribute("userPosts", userPosts);

        return "member_post_list";
    }
    //글 상세
    @GetMapping(value = "/{id}")
    public String readPost(Model model, @PathVariable("id") Long id, CommentForm commentForm) {
        Post post = this.postService.readPost(id);
        model.addAttribute("Post", post);
        return "post_detail";
    }

    //글 작성
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String createPost(PostForm postForm) {
        return "post_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String createPost(@Valid PostForm postForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post_form";
        }
        this.postService.createPost(postForm.getSubject(), postForm.getContent());
        return "redirect:/post/list";
    }

    //글 수정
    @GetMapping("/{id}/modify")
    @PreAuthorize("isAuthenticated()")
    public String questionModify(PostForm postForm, @PathVariable("id") Long id, Principal principal) {
        Post post = this.postService.readPost(id);
        if(!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        postForm.setSubject(post.getSubject());
        postForm.setContent(post.getContent());
        return "post_form";
    }
    @PostMapping("/{id}/modify")
    @PreAuthorize("isAuthenticated()")
    public String questionModify(@Valid PostForm postForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "post_form";
        }
        Post post = this.postService.readPost(id);
        if(!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.postService.modifyPost(post, postForm.getSubject(), postForm.getContent());
        return String.format("redirect:/post/%s", id);
    }

    //글 삭제
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String questionDelete(Principal principal, @PathVariable("id") Long id) {
        Post post = this.postService.readPost(id);
        if (!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.postService.deletePost(post);
        return "redirect:/post/list";
    }

    //특정 회원의 글 모아보기
    @GetMapping("/b/{username}")
    public String getUserPosts(@PathVariable String username, Model model) {
        List<Post> userPosts = postService.getPostsByMember(username);
        model.addAttribute("userPosts", userPosts);
        return "member_post_list";
    }
    @GetMapping("/b/{username}/{id}")
    public String getUserPost(@PathVariable String username, Model model, @PathVariable("id") Long id) {
        List<Post> userPosts = postService.getPostsByMember(username);
        model.addAttribute("userPosts", userPosts);
        return "post_detail";
    }
}
