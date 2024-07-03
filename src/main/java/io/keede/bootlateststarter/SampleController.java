package io.keede.bootlateststarter;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
* @author keede
* Created on 2023/08/23
*/
@Controller
public class SampleController {

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        System.out.println("SecurityContextHolder.getContext().getAuthentication() = " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("ss : " +request.getSession().getAttribute("SPRING_SECURITY_CONTEXT"));
        return "index";
    }

    @GetMapping("/auth/test")
    @ResponseBody
    public String get() {
        System.out.println("SecurityContextHolder.getContext().getAuthentication() = " + SecurityContextHolder.getContext().getAuthentication());
        return "로그인 완료";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
