package com.example.backend.controler;

import com.example.backend.constants.ActualRequest;
import com.example.backend.constants.COLOR;
import com.example.backend.constants.Constants;
import com.example.backend.service.ApiShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TEST {

    ApiShopService apiShopService;
    Constants constants;
    ActualRequest actualRequest;
    public TEST(ApiShopService apiShopService, Constants constants, ActualRequest actualRequest) {
        this.constants = constants;
        this.apiShopService = apiShopService;
        this.actualRequest = actualRequest;
    }

    @GetMapping("/TEST")
    public ResponseEntity<?> GetTest(){
        COLOR color = COLOR.RED;
        actualRequest.addValue("meters",15d);
        return  apiShopService.requestToApiShop(1,color);
    }
}
