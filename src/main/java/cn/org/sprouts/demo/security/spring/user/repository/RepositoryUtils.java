package cn.org.sprouts.demo.security.spring.user.repository;

import cn.org.sprouts.demo.security.spring.user.domain.MyUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class RepositoryUtils {
    static List<MyUser> users = new ArrayList<>();
    static {
        //密码加密工具
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);

        //创建用户并设置用户角色
        ArrayList<SimpleGrantedAuthority> userAuth = new ArrayList<>();
        userAuth.add(new SimpleGrantedAuthority("MEMBER"));
        users.add(new MyUser("user", passwordEncoder.encode("123"), userAuth));

        ArrayList<SimpleGrantedAuthority> merchantAuth = new ArrayList<>();
        merchantAuth.add(new SimpleGrantedAuthority("MERCHANT"));
        users.add(new MyUser("merchant", passwordEncoder.encode("123"), merchantAuth));

        ArrayList<SimpleGrantedAuthority> adminAuth = new ArrayList<>();
        adminAuth.add(new SimpleGrantedAuthority("ADMIN"));
        users.add(new MyUser("admin", passwordEncoder.encode("123"), adminAuth));

    }

    static MyUser findUserByUsername(String username) {
        for (MyUser user : users) {
            if(StringUtils.equals(username, user.getUsername()))
                return user;
        }
        throw new UsernameNotFoundException(username);
    }
}
