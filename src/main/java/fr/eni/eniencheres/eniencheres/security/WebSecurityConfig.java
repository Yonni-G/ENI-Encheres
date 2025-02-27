package fr.eni.eniencheres.eniencheres.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/connexion", "/login", "/login/**", "/inscription", "/images/**", "/css/**",
                                "/encheres", "/encheres/**").permitAll()
                        //.requestMatchers(HttpMethod.GET, "/jeux", "/jeux/*/afficher").permitAll()
                        //.requestMatchers("/*/hello").hasAnyRole(null)
                        //.requestMatchers("/*/ajouter", "/*/modifier", "/*/supprimer","/*/enregistrer").hasAnyRole("ADMIN", "EMPLOYE")
                        // .requestMatchers(HttpMethod.POST).hasAnyRole("ADMIN", "EMPLOYE")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/connexion")
                        .permitAll()
                        .defaultSuccessUrl("/", true)  // Redirige vers une page accessible après la connexion
                )
                .logout((logout) -> logout.permitAll()
                        .logoutSuccessUrl("/connexion"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
        //NoOpPasswordEncoder si on ne veut pas chiffrer les mots de passe !!
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}

