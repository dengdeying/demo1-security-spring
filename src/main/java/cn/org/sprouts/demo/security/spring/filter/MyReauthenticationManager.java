package cn.org.sprouts.demo.security.spring.filter;

import cn.org.sprouts.demo.security.spring.user.domain.SecurityUser;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class MyReauthenticationManager implements AuthenticationManager {
    private static final Logger log = LoggerFactory.getLogger(MyReauthenticationManager.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getPrincipal().getClass()))
            throw new AuthenticationServiceException("Cannot authenticate " + authentication);

        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        //设置最后一次访问时间
        user.setLastAccessTimeMillis(System.currentTimeMillis());

        //TODO 其他的认证逻辑
        log.info("最后一次访问时间：{}", DateFormatUtils.format(user.getLastAccessTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS"));

        return authentication;
    }

    public boolean supports(Class<?> clazz) {
        return SecurityUser.class.isAssignableFrom(clazz);
    }

}
