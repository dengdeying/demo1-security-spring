package club.deying.demo.security.spring.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

public class MySecurityInterceptorFilter extends FilterSecurityInterceptor implements Filter {
    private static final Logger log = LoggerFactory.getLogger(MySecurityInterceptorFilter.class);
    private static final String FILTER_APPLIED = "__spring_security_mySecurityInterceptorFilter_filterApplied";

    private final MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;

    public MySecurityInterceptorFilter(MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource,
            MyAccessDecisionManager myAccessDecisionManager,
            MyReauthenticationManager myAuthenticationManager) {
        this.myFilterInvocationSecurityMetadataSource = myFilterInvocationSecurityMetadataSource;
        super.setAccessDecisionManager(myAccessDecisionManager);

        setReauthenticate(myAuthenticationManager);
    }

    /**
     * <ul>
     *      <li>supper.alwaysReauthenticate 默认值为false。</li>
     *      <li>supper.authenticationManager 默认值为AbstractSecurityInterceptor.NoOpAuthenticationManager</li>
     *      <li>当setAlwaysReauthenticate(true)时，必须重写AuthenticationManager实现，用于每次重新认证。</li>
     * </ul>
     *
     * @param myAuthenticationManager
     */
    private void setReauthenticate(MyReauthenticationManager myAuthenticationManager) {
        super.setAlwaysReauthenticate(true);
        super.setAuthenticationManager(myAuthenticationManager);
    }

    @PostConstruct
    public void init() {
        log.info("设置鉴权决策管理器");
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("init filter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("[自定义过滤器]：{}", "doing filter");
        try {
            FilterInvocation filterInvocation = new FilterInvocation(servletRequest, servletResponse, filterChain);
            invoke(filterInvocation);
        } finally {
            log.info("[自定义过滤器]：{}", "done filter");
        }
    }

    @Override
    public void destroy() {
        log.info("filer destroy");
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.myFilterInvocationSecurityMetadataSource;
    }
}
