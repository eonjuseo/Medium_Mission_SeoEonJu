package com.ll.medium.domain.home.home.controller;

import org.springframework.ui.Model;
import com.ll.medium.domain.post.Post;
import com.ll.medium.domain.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @Autowired
    private final PostService postService;

    @GetMapping("/")
    public String showMain(Model model) {
        List<Post> latestPosts = this.postService.getLatest30Posts();
        model.addAttribute("latestPosts", latestPosts);
        return "domain/home/home/main";
    }
}