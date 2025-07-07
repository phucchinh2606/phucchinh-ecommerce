package com.phucchinh.ecommerce.serivce.interf;

import com.phucchinh.ecommerce.dto.AddressDto;
import com.phucchinh.ecommerce.dto.Response;
import com.phucchinh.ecommerce.entity.Address;

public interface AddressService {

    Response saveAndUpdateAddress(AddressDto addressDto);
}
