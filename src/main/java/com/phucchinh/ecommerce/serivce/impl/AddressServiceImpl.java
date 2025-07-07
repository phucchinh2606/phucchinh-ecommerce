package com.phucchinh.ecommerce.serivce.impl;

import com.phucchinh.ecommerce.dto.AddressDto;
import com.phucchinh.ecommerce.dto.Response;
import com.phucchinh.ecommerce.entity.Address;
import com.phucchinh.ecommerce.entity.User;
import com.phucchinh.ecommerce.repository.AddressRepo;
import com.phucchinh.ecommerce.serivce.interf.AddressService;
import com.phucchinh.ecommerce.serivce.interf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;
    private final UserService userService;

    @Override
    public Response saveAndUpdateAddress(AddressDto addressDto) {
        User user = userService.getLoginUser();
        Address address = user.getAddress();
        if(address==null){
            address = new Address();
            address.setUser(user);
        }
        if(addressDto.getStreet()!=null){
            address.setStreet(addressDto.getStreet());
        }
        if(addressDto.getCity()!=null){
            address.setCity(addressDto.getCity());
        }
        if(addressDto.getState()!=null){
            address.setState(addressDto.getState());
        }
        if(addressDto.getZipCode()!=null){
            address.setZipCode(addressDto.getZipCode());
        }
        if(addressDto.getCountry()!=null){
            address.setCountry(addressDto.getCountry());
        }

        addressRepo.save(address);
        String message = (user.getAddress()==null)?"Address created successfully":"Address updated successfully";

        return Response.builder()
                .status(200)
                .message(message)
                .build();
    }
}
