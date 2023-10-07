package was.backwas.ide.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import was.backwas.member.domain.LoginMemberResponse;
import was.backwas.member.domain.MemberResponse;
import was.backwas.member.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class OwnerController {

    private final MemberService memberService;

    @GetMapping("/ownercheck")
    public String ownerCheck(HttpServletRequest request, @SessionAttribute(value = "loginMember", required = false) LoginMemberResponse loginMemberResponse) {
        HttpSession session = request.getSession(false);

        if (session == null || loginMemberResponse == null) {
            return "비로그인";
        }

        MemberResponse response = memberService.getUUID(loginMemberResponse.getEmail());
        return (String) response.getData();

    }
}
