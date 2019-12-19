package club.deying.demo.security.spring.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MyAccessDecisionManager implements AccessDecisionManager {
    private static final Logger log = LoggerFactory.getLogger(MyAccessDecisionManager.class);

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        log.info("****************************************权限鉴定********************************************");
        /*FilterInvocation filterInvocation = (FilterInvocation) object; // object 是一个URL
        log.info("[当前路径[{}]需要的资源权限]：{}",filterInvocation.getRequestUrl(),configAttributes);*/
        log.info("[登录用户[{}]权限]：{}", authentication.getName(), authentication.getAuthorities());

        if (configAttributes == null) {
            return;
        }

        for (ConfigAttribute configAttribute : configAttributes) {
            /* 资源的权限 */
            String attribute = configAttribute.getAttribute();
            /* 用户的权限 */
            for (GrantedAuthority authority : authentication.getAuthorities()) { // 当前用户的权限
                if (authority.getAuthority().trim().equals("ROLE_ANONYMOUS")) return;
                log.info("[资源角色==用户角色] ？ {} == {}", attribute.trim(), authority.getAuthority().trim());
                if (attribute.trim().equals(authority.getAuthority().trim())) {
                    log.info("[鉴权决策管理器]：登录用户[{}]权限匹配", authentication.getName());
                    return;
                }
            }
        }
        log.info("[鉴权决策管理器]：登录用户[{}]权限不足", authentication.getName());
        throw new AccessDeniedException("权限不足");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
