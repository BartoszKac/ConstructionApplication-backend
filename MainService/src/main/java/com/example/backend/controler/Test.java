package com.example.backend.controler;

import com.example.backend.constants.ActualRequest;
import com.example.backend.constants.COLOR;
import com.example.backend.model.AreaSetRequest;
import com.example.backend.service.ApiShopService;
import com.example.backend.service.PainService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class Test {


    private ApiShopService apiShopService;
    private ActualRequest actualRequest;

    public Test(PainService painService, ApiShopService apiShopService,ActualRequest actualRequest) {
        this.apiShopService = apiShopService;

        this.actualRequest = actualRequest;
    }

    @GetMapping ("/test")
    public ResponseEntity<?> sendAreSet() {

        //actualRequest.addValue("meters",60d);

        return apiShopService.requestToApiShop(60d, COLOR.WHITE);
    }

}
