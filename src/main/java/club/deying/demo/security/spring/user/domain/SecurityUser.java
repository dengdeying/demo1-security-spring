package club.deying.demo.security.spring.user.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecurityUser extends User implements UserDetails {
    private List<Role> roles = new ArrayList<>();



    public SecurityUser(String username, String password, Collection<Role> authorities) {
        super(username, password, authorities);
    }



    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> rs = new ArrayList<>();

        for (Role role : roles) {
            rs.add(new SimpleGrantedAuthority(role.getName()));
        }

        return rs;
    }
}
