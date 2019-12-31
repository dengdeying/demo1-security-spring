package cn.org.sprouts.demo.security.spring.filter;

import cn.org.sprouts.demo.security.spring.user.domain.Privilege;
import cn.org.sprouts.demo.security.spring.user.domain.Role;
import cn.org.sprouts.demo.security.spring.user.repository.PrivilegeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private static final Logger log = LoggerFactory.getLogger(MyFilterInvocationSecurityMetadataSource.class);

    private PrivilegeRepository privilegeRepository;

    private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();

    public MyFilterInvocationSecurityMetadataSource(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    @PostConstruct
    public void init() {
        log.info("【自定义权限资源】：{}", "初始化权限资源");
        List<Privilege> privileges = privilegeRepository.getAll();
        for (Privilege privilege : privileges) {
            List<Role> roles = privilege.getRoles();
            List<ConfigAttribute> configAttributes = new ArrayList<>();
            for (Role role : roles) {
                configAttributes.add(new SecurityConfig(role.getName()));
            }
            requestMap.put(new AntPathRequestMatcher(privilege.getUrl()), configAttributes);
        }
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        log.info("【自定义权限资源】：{}", "获取本次访问需要的权限");
        final HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                log.info("【自定义权限资源】：当前路径[{}]需要的资源权限:[{}] ==> 触发鉴权决策管理器", entry.getKey(), entry.getValue().toString());
                return entry.getValue();
            }
        }
        log.info("【自定义权限资源】：{}==> 本次访问无需权限", request.getRequestURI());
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }
        log.info("【自定义权限资源】：获取所有的角色==> {}", allAttributes.toString());
        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
