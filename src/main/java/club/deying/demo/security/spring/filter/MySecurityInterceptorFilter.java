package club.deying.demo.security.spring.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

@Component
public class MySecurityInterceptorFilter extends FilterSecurityInterceptor implements Filter {
    private static final Logger log = LoggerFactory.getLogger(MySecurityInterceptorFilter.class);

    private final MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;
    private final MyAccessDecisionManager myAccessDecisionManager;

    @Autowired
    public MySecurityInterceptorFilter(MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource, MyAccessDecisionManager myAccessDecisionManager) {
        this.myFilterInvocationSecurityMetadataSource = myFilterInvocationSecurityMetadataSource;
        this.myAccessDecisionManager = myAccessDecisionManager;
    }

    @PostConstruct
    public void init() {
        log.info("设置鉴权决策管理器");
        super.setAccessDecisionManager(myAccessDecisionManager);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("init filter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("[自定义过滤器]：{}", "CustomFilterSecurityInterceptor.doFilter()");
        FilterInvocation filterInvocation = new FilterInvocation(servletRequest, servletResponse, filterChain);
        invoke(filterInvocation);
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
