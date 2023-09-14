package com.example.springsecuritylessen.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    //Authenticatie
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();//alleen encryptie
    }

    @Bean
    //Maakt een method die een service retourneerd die userdetails aanlevert
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        //Houdt de credentials in het geheugen vast. Een implementatie v UserDetailsService interface
        InMemoryUserDetailsManager man = new InMemoryUserDetailsManager();

        //Maken v userDetail objecten wat toegest gebruikers is
        UserDetails u1 = User
            .withUsername("karel")
            //gebruikt de encoder om de password te encoderen
            .password(encoder.encode("appel"))
            //naam van roles kan je zelf verzinnen
            .roles("USER", "CREATOR")
            .build();
        man.createUser(u1);

        UserDetails u2 = User
                .withUsername("Hans")
                //gebruikt de encoder om de password te encoderen
                .password(encoder.encode("peer"))
                //naam van roles kan je zelf verzinnen
                .roles("Admin")
                .build();
        man.createUser(u2);

        return man;
    }
    //Authorisatie
    @Bean
    //throws Exception is nodig vir httpBasic method wat exception kan veroorzaak. Method kan mogelijk een exeption..
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //HttpSecurity is a bean
        http

            //Deprecated. Hij doet het wel, maar hij gaan in toekomst vervangt worden
            .httpBasic()
            //Om weer terug te komen op een HttpSecurity object
            .and()
            //Om request machers te definieeren
            .authorizeHttpRequests()
            //Alleen user met ADMIN role kan erbij. HttpMethod is optioneel
            .requestMatchers(HttpMethod.GET, "/secret").hasRole("ADMIN")
            .requestMatchers("/hello").permitAll()
            //"/authenticated - je moet geauthenticeerd zijn
            .requestMatchers("/**").authenticated()
            //Vangnet die we kunnen toevoegen
            .anyRequest().denyAll()
            .and()
            //Stateless - We willen geen cookies
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf().disable();

        return http.build();
    }
}
