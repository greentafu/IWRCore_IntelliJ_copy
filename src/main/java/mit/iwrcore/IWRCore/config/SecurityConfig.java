package mit.iwrcore.IWRCore.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("---------------filterChain-----------------");

        http.authorizeRequests()
                .requestMatchers("/assets/**", "/fonts/**", "/js/**", "/libs/**", "/line/**", "/scss/**", "/tasks/**").permitAll()
                .requestMatchers("/login", "/checkrole", "/logout").permitAll()
                .requestMatchers("/manager/**").hasRole("MANAGER")
                .requestMatchers("/partner/**").hasRole("PARTNER")
                .requestMatchers("/proteam/**", "/development/**").hasAnyRole("DEV_PROD_TEAM", "MANAGER")
                .requestMatchers("/contract/**", "/goodshandling/**", "/invoice/**", "/jodal/**", "/material/**", "/order/**", "/production/**", "/progress/**").hasAnyRole("MATERIAL_TEAM", "MANAGER")
                .requestMatchers("/main", "/jago/**", "/adminpartner/**", "/category").hasAnyRole("DEV_PROD_TEAM", "MATERIAL_TEAM", "MANAGER")
                .requestMatchers("/saveReceiveShipment").hasAnyRole("MATERIAL_TEAM", "MANAGER")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        http.formLogin(formLogin->
                formLogin.loginPage("/login")
                        .defaultSuccessUrl("/checkrole", true)
                        .failureUrl("/login?error"));
        http.csrf(csrf->csrf.disable());
        http.logout(logout->
                logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll());
        http.headers().frameOptions().sameOrigin();
        http.sessionManagement().invalidSessionUrl("/main");
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }
}
