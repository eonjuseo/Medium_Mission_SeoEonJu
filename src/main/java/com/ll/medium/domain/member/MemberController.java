package com.ll.medium.domain.member;

import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/member")
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin(MemberForm memberForm) {
        return "join_form";
    }
    @PostMapping("/join")
    public String showJoin(@Valid MemberForm memberForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "join_form";
        }
        if (!memberForm.getPassword1().equals(memberForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "join_form";
        }

        try {
            memberService.join(memberForm.getUsername(),
                    memberForm.getEmail(), memberForm.getPassword1());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("joinFailed", "이미 등록된 사용자입니다.");
            return "join_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("joinFailed", e.getMessage());
            return "join_form";
        }
        return "redirect:/";
    }



    @GetMapping("/login")
    public String showLogin(MemberForm memberForm) {
        return "login_form";
    }


}
