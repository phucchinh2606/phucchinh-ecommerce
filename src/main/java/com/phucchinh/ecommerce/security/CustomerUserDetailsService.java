package com.phucchinh.ecommerce.security;

import com.phucchinh.ecommerce.dto.UserDto;
import com.phucchinh.ecommerce.ecxeption.NotFoundException;
import com.phucchinh.ecommerce.entity.User;
import com.phucchinh.ecommerce.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username)
                .orElseThrow(()->new NotFoundException("User/Email not found"));
        return AuthUser.builder()
                .user(user)
                .build();
    }
}
