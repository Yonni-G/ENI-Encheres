package fr.eni.eniencheres.eniencheres.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {


    private final AuthenticationManager authenticationManager;

    public AuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Authentication authenticate(String pseudo, String motDePasse) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(pseudo, motDePasse);
        return authenticationManager.authenticate(authentication);
    }
}

