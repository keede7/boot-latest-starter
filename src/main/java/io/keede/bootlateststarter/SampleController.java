package io.keede.bootlateststarter;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
* @author keede
* Created on 2023/08/23
*/
@Controller
public class SampleController {

    @GetMapping("/")
    public String index() {
        System.out.println("SecurityContextHolder.getContext().getAuthentication() = " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("SecurityContextHolder.getContext() = " + SecurityContextHolder.getContext());
        return "index";
    }

    @GetMapping("/auth/test")
    @ResponseBody
    public String get() {
        System.out.println("SecurityContextHolder.getContext().getAuthentication() = " + SecurityContextHolder.getContext().getAuthentication());
        return "로그인 완료";
    }

}
