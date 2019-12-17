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
    private static List<SecurityUser> users = new ArrayList<>();

    static {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);

        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
        SimpleGrantedAuthority member = new SimpleGrantedAuthority("MEMBER");
        SimpleGrantedAuthority merchant = new SimpleGrantedAuthority("MERCHANT");

        ArrayList<SimpleGrantedAuthority> userAuth = new ArrayList<>();
        userAuth.add(member);
        users.add(new SecurityUser("user", passwordEncoder.encode("password"), userAuth));

        ArrayList<SimpleGrantedAuthority> merchantAuth = new ArrayList<>();
        merchantAuth.add(merchant);
        users.add(new SecurityUser("merchant", passwordEncoder.encode("password"), merchantAuth));

        ArrayList<SimpleGrantedAuthority> adminAuth = new ArrayList<>();
        adminAuth.add(admin);
        users.add(new SecurityUser("admin", passwordEncoder.encode("password"), adminAuth));
    }

    /**
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
