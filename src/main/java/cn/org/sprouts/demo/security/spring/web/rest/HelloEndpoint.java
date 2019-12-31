package cn.org.sprouts.demo.security.spring.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloEndpoint {
    @GetMapping("/")
    public String hello() {
        return "hello, Spring Security";
    }

    @GetMapping("/admin")
    public String admin() {
        return "hello, admin";
    }

    @GetMapping("/merchant")
    public String merchant() {
        return "hello, merchant";
    }
}
