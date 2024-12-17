package fr.eni.eniencheres.eniencheres.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                        .requestMatchers("/", "/login","/login/**", "/images/**", "/css/**").permitAll()
                        //.requestMatchers(HttpMethod.GET, "/jeux", "/jeux/*/afficher").permitAll()
                        //.requestMatchers("/*/hello").hasAnyRole(null)
                        //.requestMatchers("/*/ajouter", "/*/modifier", "/*/supprimer","/*/enregistrer").hasAnyRole("ADMIN", "EMPLOYE")
                        // .requestMatchers(HttpMethod.POST).hasAnyRole("ADMIN", "EMPLOYE")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        //.loginPage("/custom-login")
                        .permitAll()
                        .defaultSuccessUrl("/", true)  // Redirige vers une page accessible après la connexion
                )
                .logout((logout) -> logout.permitAll()
                        .logoutSuccessUrl("/login"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
        //NoOpPasswordEncoder si on ne veut pas chiffrer les mots de passe !!
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails user =
//			 User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("password")
//				.roles("USER")
//				.build();
//
//		return new InMemoryUserDetailsManager(user);
//	}
}

