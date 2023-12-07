package com.ll.medium.domain.post;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/post")
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    //글 목록 조회
    @GetMapping("/list")
    public String list(Model model) {
        List<Post> postList = this.postService.getList();
        model.addAttribute("postList", postList);
        return "post_list";
    }

    //내 글 목록 조회
    //@GetMapping("/myList")

    //글 상세내용 조회
    @GetMapping(value = "/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Post post = this.postService.getPost(id);
        model.addAttribute("Post", post);
        return "post_detail";
    }

    //글 작성
    //@GetMapping("/write")
    //@PostMapping("/write")

    //글 수정
    //@GetMapping("/{id}/modify")
    //@PostMapping("/{id}/modify")

    //글 삭제
    //@DeleteMapping("/{id}/delete")

    //특정 회원의 글 모아보기
    //@GetMapping("/b/user1") //리스트
    //@GetMapping("/b/user1/3") //상세

}
