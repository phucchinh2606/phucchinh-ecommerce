package com.phucchinh.ecommerce.serivce.impl;

import com.phucchinh.ecommerce.dto.LoginRequest;
import com.phucchinh.ecommerce.dto.Response;
import com.phucchinh.ecommerce.dto.UserDto;
import com.phucchinh.ecommerce.ecxeption.InvalidCredentialsException;
import com.phucchinh.ecommerce.ecxeption.NotFoundException;
import com.phucchinh.ecommerce.entity.User;
import com.phucchinh.ecommerce.enums.UserRole;
import com.phucchinh.ecommerce.mapper.EntityDtoMapper;
import com.phucchinh.ecommerce.repository.UserRepo;
import com.phucchinh.ecommerce.security.JwtUtils;
import com.phucchinh.ecommerce.serivce.interf.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response registerUser(UserDto registrationRequest) {
        UserRole role = UserRole.USER;
        if(registrationRequest.getRole() != null && registrationRequest.getRole().equalsIgnoreCase("admin")){
            role = UserRole.ADMIN;
        }

        User user = User.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhoneNumber())
                .role(role)
                .build();

        User savedUser = userRepo.save(user);
        UserDto userDto = entityDtoMapper.mapUsertoDtoBasic(savedUser);
        return Response.builder()
                .status(200)
                .message("User successfully added")
                .user(userDto)
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user = userRepo.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new NotFoundException("Email not found"));
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Password do not match");
        }
        String token = jwtUtils.generateToken(user);
        return Response.builder()
                .status(200)
                .message("User log in successfully")
                .token(token)
                .expirationTime("6 months")
                .role(user.getRole().name())
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserDto> userDtos = users.stream().map(entityDtoMapper::mapUsertoDtoBasic)
                .toList();
        return Response.builder()
                .status(200)
                .message("Successful")
                .userList(userDtos)
                .build();
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("User Email is: "+email);
        return userRepo.findByEmail(email).orElseThrow(
                ()-> new UsernameNotFoundException("User not found")
        );
    }

    @Override
    public Response getUserInfoAndOrderHistory() {
        User user = getLoginUser();
        UserDto userDto = entityDtoMapper.mapUserToDtoPlusAddressAndOrderHistory(user);
        return Response.builder()
                .status(200)
                .user(userDto)
                .build();
    }
}
