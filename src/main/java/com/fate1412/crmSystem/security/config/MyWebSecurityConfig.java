package com.fate1412.crmSystem.security.config;

import com.fate1412.crmSystem.security.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter  {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationEntryPoint authenticationEntryPoint = new MyAuthenticationEntryPoint();
        AccessDeniedHandler accessDeniedHandler = new MyAccessDeniedHandler();
        LogoutSuccessHandler logoutSuccessHandler = new MyLogoutSuccessHandler();
        SessionInformationExpiredStrategy sessionInformationExpiredStrategy = new MySessionInformationExpiredStrategy();
        
        http.csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeRequests()
//                .antMatchers("/static/**").permitAll()
//                .antMatchers("/user/login").hasAnyAuthority("TEST_R1")
//                .antMatchers("/user/login").permitAll()
//                .anyRequest().authenticated()// 用户访问其它URL都必须认证后访问（登录后访问）
                //登录
                .and().formLogin()
//                .permitAll()//允许所有用户
                //记住我
//                .and().rememberMe()
//                .tokenValiditySeconds(60*60)
                //异常处理(权限拒绝、登录失效等)
                .and().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                
                //登出
                .and().logout()
                .permitAll()//允许所有用户
                .logoutSuccessHandler(logoutSuccessHandler)//登出成功处理逻辑
                .deleteCookies("JSESSIONID")//登出之后删除cookie
                //账号登录限制
                .and().sessionManagement()
                .maximumSessions(1)
                .expiredSessionStrategy(sessionInformationExpiredStrategy);
        
        http.addFilterBefore(usernamePasswordAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
    }
    

    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }
    
    /**
     * 自定义登录拦截器
     */
    @Bean
    MyUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
        AuthenticationSuccessHandler authenticationSuccessHandler = new MyAuthenticationSuccessHandler();
        AuthenticationFailureHandler authenticationFailureHandler = new MyAuthenticationFailureHandler();
        MyUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new MyUsernamePasswordAuthenticationFilter(authenticationManagerBean());
        
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);//登录成功处理逻辑
        usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);//登录失败处理逻辑
        usernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return usernamePasswordAuthenticationFilter;
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
    
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        
        CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedHeader(CorsConfiguration.ALL);
        configuration.addAllowedMethod(CorsConfiguration.ALL);
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
