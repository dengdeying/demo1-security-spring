package cn.org.sprouts.demo.security.spring.configuration.security;

import cn.org.sprouts.demo.security.spring.user.service.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private LoginSucessHandler loginSucessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private SecurityUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()/*设置认证请求URL*/
                .antMatchers(HttpMethod.GET,"/home", "/login", "/login.html").permitAll()/*设置无需认证的URL*/
                .anyRequest().authenticated()/*任何URL都需要认证*/

                .and()/* 完成上一个配置，进行下一步配置 */

                .formLogin()/* 配置表单登录 */
                .usernameParameter("username") /* 默认值 username */
                .passwordParameter("password") /* 默认值 password */
                .loginPage("/login") /* 设置登录页面，默认是HTTP GET /login */
                .loginProcessingUrl("/login") /* 设置登录页面，默认是HTTP POST /login */
                .successHandler(loginSucessHandler)
                .failureHandler(loginFailureHandler)

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
}
