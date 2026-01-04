package com.wesleypatrick.jwtsecurity.security.auth;

import com.wesleypatrick.jwtsecurity.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthConfigService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthConfigService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserDetailsByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
