package com.idgs06.UTEQLive.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Metodo para definir el metodo de encriptación de password
     *
     * @return
     */
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * Filtro para validar los permisos de las peticiones o rutas
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> {
                    authz.requestMatchers("/registro/**").permitAll()
                            .requestMatchers("/recuperar-pass/**").permitAll()
                            .requestMatchers("/").hasRole("ESTUDIANTE")
                            .requestMatchers("/comentario/**").hasRole("ESTUDIANTE")
                            .requestMatchers("/publicacion/**").hasRole("ESTUDIANTE")
                            .requestMatchers("/amigos/**").hasRole("ESTUDIANTE")
                            .requestMatchers("/solicitudes/**").hasRole("ESTUDIANTE")
                            .requestMatchers("/perfil/**").hasRole("ESTUDIANTE")
                            .requestMatchers("/usuarios/guardar/**").hasRole("ESTUDIANTE")
                            .requestMatchers("/usuarios/").hasAnyRole("ADMIN", "MAESTRO")
                            .requestMatchers("/usuarios/**").hasRole("ADMIN")
                            .requestMatchers("/resources/**").permitAll()
                            .requestMatchers("/static/**").permitAll()
                            .requestMatchers("/img/**").permitAll()
                            .anyRequest().authenticated();
                }
                )
                .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();

    }

}
