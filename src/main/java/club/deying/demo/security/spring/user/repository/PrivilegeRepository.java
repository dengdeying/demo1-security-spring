package club.deying.demo.security.spring.user.repository;

import club.deying.demo.security.spring.user.domain.Privilege;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrivilegeRepository {
    public List<Privilege> getAll() {
        return RepositoryUtils.getAllPrivilege();
    }
}
