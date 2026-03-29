package com.pinhaDev.worknest.services;

import com.pinhaDev.worknest.config.TokenConfig;
import com.pinhaDev.worknest.dto.request.LoginRequest;
import com.pinhaDev.worknest.dto.request.RegisterRequest;
import com.pinhaDev.worknest.domain.models.User;
import com.pinhaDev.worknest.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthService(
            UserRepository userRepository,
            BCryptPasswordEncoder encoder,
            AuthenticationManager authenticationManager,
            TokenConfig tokenConfig
    ) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    public void register(RegisterRequest request) {
        checkPasswordConfirmation(request.password(), request.passwordConfirmation());
        checkIfUserExists(request.email());

        var passwordHash = encoder.encode(request.password());
        var user = User.builder()
                .email(request.email())
                .password(passwordHash)
                .build();

        userRepository.save(user);
    }

    public String login(LoginRequest request) {

        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );

        Authentication authentication = authenticationManager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();

        return tokenConfig.generateToken(user.getEmail());
    }

    private void checkPasswordConfirmation(String password, String passwordConfirmation) {
        if(!password.equals(passwordConfirmation)) {
            throw new IllegalArgumentException("As senhas devem ser iguais");
        }
    }

    private void checkIfUserExists(String email) {
        var userExists = userRepository.findByEmail(email);

        if(userExists.isPresent()) {
            throw new IllegalArgumentException("Usuário já existe");
        }
    }
}
