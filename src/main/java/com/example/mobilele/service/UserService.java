package com.example.mobilele.service;

import com.example.mobilele.model.dto.UserRegisterDTO;
import com.example.mobilele.model.entity.UserEntity;
import com.example.mobilele.model.mapper.UserMapper;
import com.example.mobilele.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class UserService {

    private Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper,
                       UserDetailsService userDetailsService,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userDetailsService = userDetailsService;
        this.emailService = emailService;
    }

    public void registerAndLogin(UserRegisterDTO userRegisterDTO,
                                 Locale preferredLocale) {
        String password = userRegisterDTO.getPassword();
        UserEntity newUser = userMapper.userDtoToUserEntity(userRegisterDTO);
        String encodePassword = passwordEncoder.encode(password);
        newUser.setPassword(encodePassword);

        userRepository.save(newUser);
        login(newUser.getEmail());
        emailService.sendRegistrationEmail(
                newUser.getEmail(),
                newUser.getFirstName() + " " + newUser.getLastName(),
                preferredLocale);
    }

    public void login(String userName) {
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(userName);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.
                getContext().
                setAuthentication(auth);
    }

    public void createUserIfNotExist(String email) {
        var userOpt = this.userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            var newUser = new UserEntity().
                    setEmail(email).
                    setPassword(null).
                    setFirstName("New").
                    setLastName("User").
                    setUserRoles(List.of());
            userRepository.save(newUser);
        }
    }
}
