package club.deying.demo.security.spring.user.domain;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SecurityUser extends User implements UserDetails {
    //最后一次访问时间
    private long lastAccessTimeMillis;

    public SecurityUser(String username, String password, Collection<Role> authorities) {
        super(username, password, authorities);
    }

    public long getLastAccessTimeMillis() {
        return lastAccessTimeMillis;
    }

    public void setLastAccessTimeMillis(long lastAccessTimeMillis) {
        this.lastAccessTimeMillis = lastAccessTimeMillis;
    }
}
