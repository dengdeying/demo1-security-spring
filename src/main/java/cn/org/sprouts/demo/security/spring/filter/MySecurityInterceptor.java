package cn.org.sprouts.demo.security.spring.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

public class MySecurityInterceptor extends FilterSecurityInterceptor implements Filter {
    private static final Logger log = LoggerFactory.getLogger(MySecurityInterceptor.class);
    private static final String FILTER_APPLIED = "__spring_security_mySecurityInterceptorFilter_filterApplied";

    private final MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;

    public MySecurityInterceptor(MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource,
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

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("【自定义鉴权拦截器】:{}", "初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        UUID id = UUID.randomUUID();
        log.info("【自定义鉴权拦截器】{}：{}; 访问地址：{}", id, "开始执行过滤", ((HttpServletRequest)servletRequest).getRequestURI());
        try {
            FilterInvocation filterInvocation = new FilterInvocation(servletRequest, servletResponse, filterChain);
            invoke(filterInvocation);
        } finally {
            log.info("【自定义鉴权拦截器】{}：{}", id, "完成本次过滤");
        }
    }

    @Override
    public void destroy() {
        log.info("【自定义鉴权拦截器】:{}", "销毁");
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.myFilterInvocationSecurityMetadataSource;
    }
}
