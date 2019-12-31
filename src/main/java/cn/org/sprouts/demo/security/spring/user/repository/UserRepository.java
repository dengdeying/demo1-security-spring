package cn.org.sprouts.demo.security.spring.user.repository;

import cn.org.sprouts.demo.security.spring.user.domain.SecurityUser;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {
    public SecurityUser findUserByUsername(String username) {
        return RepositoryUtils.findUserByUsername(username);
    }
}
