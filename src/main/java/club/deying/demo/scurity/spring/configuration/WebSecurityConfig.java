package club.deying.demo.scurity.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private LoginSucessHandler loginSucessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
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
}
