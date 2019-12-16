package club.deying.demo.security.spring.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloEndpoint {
    @GetMapping("/")
    public String hello() {
        return "hello, spring security";
    }
}
