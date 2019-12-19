package club.deying.demo.security.spring.user.repository;

import club.deying.demo.security.spring.user.domain.Privilege;
import club.deying.demo.security.spring.user.domain.Role;
import club.deying.demo.security.spring.user.domain.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class RepositoryUtils {
    static List<Role> roles = new ArrayList<>();
    static List<Privilege> privileges = new ArrayList<>();
    static List<SecurityUser> users = new ArrayList<>();
    static {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);

        Privilege pHello = new Privilege("/", "hello", "hello");
        Privilege pAdmin = new Privilege("/admin", "hello", "hello");
        Privilege pMerchant = new Privilege("/merchant", "hello", "hello");

        Role rAdmin = new Role("ADMIN", "admin");
        setPrivileges(rAdmin, pAdmin);
        roles.add(rAdmin);
        Role rMember = new Role("MEMBER", "member");
        setPrivileges(rMember, pHello);
        roles.add(rMember);
        Role rMerchant = new Role("MERCHANT", "merchant");
        setPrivileges(rMerchant, pMerchant);
        roles.add(rMerchant);

        ArrayList<Role> userAuth = new ArrayList<>();
        userAuth.add(rMember);
        users.add(new SecurityUser("user", passwordEncoder.encode("password"), userAuth));

        ArrayList<Role> adminAuth = new ArrayList<>();
        adminAuth.add(rAdmin);
        users.add( new SecurityUser("admin", passwordEncoder.encode("password"), adminAuth));

        ArrayList<Role> merchantAuth = new ArrayList<>();
        merchantAuth.add(rMerchant);
        users.add(new SecurityUser("merchant", passwordEncoder.encode("password"), merchantAuth));

    }

    static void setPrivileges(Role role, Privilege ... privileges) {
        List<Privilege> rPrivileges = role.getPrivileges();

        for (Privilege privilege : privileges) {
            List<Role> pRoles = privilege.getRoles();
            rPrivileges.add(privilege);
            pRoles.add(role);

        }
    }

    static List<Privilege> getAllPrivilege() {
        return privileges;
    }

    static SecurityUser findUserByUsername(String username) {
        for (SecurityUser user : users) {
            if(StringUtils.equals(username, user.getUsername()))
                return user;
        }
        throw new UsernameNotFoundException(username);
    }
}
