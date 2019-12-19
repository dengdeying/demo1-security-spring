package club.deying.demo.security.spring.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

@Component
public class MySecurityInterceptorFilter extends AbstractSecurityInterceptor implements Filter {
    private static final Logger log = LoggerFactory.getLogger(MySecurityInterceptorFilter.class);

    private static final String FILTER_APPLIED = "__spring_security_CustomFilterSecurityInterceptor_filterApplied";

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
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init filter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("[自定义过滤器]：{}", "CustomFilterSecurityInterceptor.doFilter()");
        FilterInvocation filterInvocation = new FilterInvocation(servletRequest, servletResponse, filterChain);
        invoke(filterInvocation);
    }

    private void invoke(FilterInvocation fi) throws IOException, ServletException {
        if ((fi.getRequest() != null) && (fi.getRequest().getAttribute(FILTER_APPLIED) != null)) {
            // filter already applied to this request and user wants us to observe
            // once-per-request handling, so don't re-do security checking
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } else {
            // first time this request being called, so perform security checking
            if (fi.getRequest() != null) {
                fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
            }

            /* 调用父类的beforeInvocation ==> accessDecisionManager.decide(..) */
            InterceptorStatusToken token = super.beforeInvocation(fi);

            try {
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            } finally {
                super.finallyInvocation(token);
            }
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {
        log.info("filer==========================================destroy");
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.myFilterInvocationSecurityMetadataSource;
    }
}
