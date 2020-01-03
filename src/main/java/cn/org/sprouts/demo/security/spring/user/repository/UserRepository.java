package cn.org.sprouts.demo.security.spring.user.repository;

import cn.org.sprouts.demo.security.spring.user.domain.MyUser;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {
    public MyUser findUserByUsername(String username) {
        return RepositoryUtils.findUserByUsername(username);
    }
}
