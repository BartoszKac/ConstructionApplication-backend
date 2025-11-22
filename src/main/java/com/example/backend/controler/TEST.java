package com.example.backend.controler;

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
    public TEST(ApiShopService apiShopService, Constants constants) {
        this.constants = constants;
        this.apiShopService = apiShopService;
    }

    @GetMapping("/TEST")
    public ResponseEntity<?> GetTest(){
        COLOR color = COLOR.WHITE;
        return  apiShopService.requestToApiShop(1,color);
    }
}
