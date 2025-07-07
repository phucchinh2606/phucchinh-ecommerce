package com.phucchinh.ecommerce.serivce.interf;

import com.phucchinh.ecommerce.dto.LoginRequest;
import com.phucchinh.ecommerce.dto.Response;
import com.phucchinh.ecommerce.dto.UserDto;
import com.phucchinh.ecommerce.entity.User;

public interface UserService {
    Response registerUser(UserDto registrationRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    User getLoginUser();
    Response getUserInfoAndOrderHistory();
}
