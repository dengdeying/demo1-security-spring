package club.deying.demo.security.spring.configuration.security;

import club.deying.demo.security.spring.filter.MyAccessDecisionManager;
import club.deying.demo.security.spring.filter.MyReauthenticationManager;
import club.deying.demo.security.spring.filter.MyFilterInvocationSecurityMetadataSource;
import club.deying.demo.security.spring.filter.MySecurityInterceptorFilter;
import club.deying.demo.security.spring.user.service.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private LoginSucessHandler loginSucessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private SecurityUserDetailsService userDetailsService;
    @Autowired
    private MySecurityInterceptorFilter mySecurityInterceptorFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAt(mySecurityInterceptorFilter, FilterSecurityInterceptor.class)

                .authorizeRequests()/*设置认证请求URL*/
                .antMatchers("/login", "/home").permitAll()/*设置无需认证的URL*/
                .anyRequest().authenticated()/*任何URL都需要认证*/

                .and()/* 完成上一个配置，进行下一步配置 */

                .formLogin()/* 配置表单登录 */
                .loginPage("/login")/* 设置登录页面 */
                .successHandler(loginSucessHandler) /* 设置成功处理器 */
                .failureHandler(loginFailureHandler)/* 设置失败处理器*/

                .and()/* 完成上一个配置，进行下一步配置 */

                .logout()/* 配置表单登录 */
                .logoutSuccessUrl("/home");/* 设置退出页面 */


    }

    /**
     * spring5.0之后，spring security必须设置加密方法否则会报
     * There is no PasswordEncoder mapped for the id "null"
     *
     * @return 加密
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean(name = "mySecurityInterceptorFilter")
    public MySecurityInterceptorFilter mySecurityInterceptorFilter(final MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource,
            final MyAccessDecisionManager myAccessDecisionManager,
            final MyReauthenticationManager myAuthenticationManager) {
        return new MySecurityInterceptorFilter(myFilterInvocationSecurityMetadataSource, myAccessDecisionManager, myAuthenticationManager);
    }

    /**
     * We do this to ensure our Filter is only loaded once into Application Context
     */
    @Bean(name = "authenticationFilterRegistration")
    public FilterRegistrationBean myAuthenticationFilterRegistration(final MySecurityInterceptorFilter mySecurityInterceptorFilter) {
        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(mySecurityInterceptorFilter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }
}
