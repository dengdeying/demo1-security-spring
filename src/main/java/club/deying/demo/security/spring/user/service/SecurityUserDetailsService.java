package club.deying.demo.security.spring.user.service;

import club.deying.demo.security.spring.user.domain.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityUserDetailsService implements UserDetailsService {

    //模拟数据源
    private static List<SecurityUser> users = new ArrayList<>();

    static {
        //密码加密工具
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);

        //创建用户并设置用户角色
        ArrayList<SimpleGrantedAuthority> userAuth = new ArrayList<>();
        userAuth.add(new SimpleGrantedAuthority("MEMBER"));
        users.add(new SecurityUser("user", passwordEncoder.encode("123"), userAuth));

        ArrayList<SimpleGrantedAuthority> merchantAuth = new ArrayList<>();
        merchantAuth.add(new SimpleGrantedAuthority("MERCHANT"));
        users.add(new SecurityUser("merchant", passwordEncoder.encode("123"), merchantAuth));

        ArrayList<SimpleGrantedAuthority> adminAuth = new ArrayList<>();
        adminAuth.add(new SimpleGrantedAuthority("ADMIN"));
        users.add(new SecurityUser("admin", passwordEncoder.encode("123"), adminAuth));
    }

    /**
     * 根据用户名从数据源中检索用户信息
     *
     * @param username
     *
     * @return
     *
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username))
            throw new IllegalArgumentException("username is empty");

        for (SecurityUser user : users) {
            if (StringUtils.equalsAnyIgnoreCase(username, user.getUsername()))
                return user;
        }
        throw new UsernameNotFoundException(username);
    }
}
