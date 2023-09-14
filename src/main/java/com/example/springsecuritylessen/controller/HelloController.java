package com.example.springsecuritylessen.controller;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        //Authenticatie object bevat alle inform over ingelogde gebruiker
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //if - omdat we niet zeker weten of iemand ingelog is. Je vraagt of er iemand is ingelogd
        if (auth.getPrincipal() instanceof UserDetails) {
            //resultaat v getPrincipal object typecasten wij naar een UserDetails object
            UserDetails ud = (UserDetails) auth.getPrincipal();
            return "Hello there!" + ud.getUsername();
        } else {
            //Willen checken of huidige gebruiker eigenaar is van entiteit. Bv als je je gegevens willen wijzigen
            return "Hello Stranger";
        }


    }

    @GetMapping("/secret")
    public  String tellSecret() {
        return "this is a secret";
    }

}
