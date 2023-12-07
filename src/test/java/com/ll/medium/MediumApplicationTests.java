package com.ll.medium;

import com.ll.medium.domain.post.Post;
import com.ll.medium.domain.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class MediumApplicationTests {

    @Autowired
    private PostRepository postRepository;

    @Test
    void testJpa() {
        Post a1 = new Post();
        a1.setSubject("sbb가 무엇인가요?");
        a1.setContent("sbb에 대해서 알고 싶습니다.");
        a1.setCreateDate(LocalDateTime.now());
        this.postRepository.save(a1);  // 첫번째 질문 저장

        Post a2 = new Post();
        a2.setSubject("스프링부트 모델 질문입니다.");
        a2.setContent("id는 자동으로 생성되나요?");
        a2.setCreateDate(LocalDateTime.now());
        this.postRepository.save(a2);  // 두번째 질문 저장
    }
}
