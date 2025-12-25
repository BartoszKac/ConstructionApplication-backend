package com.example.backend.controler;


import com.example.backend.constants.ActualRequest;
import com.example.backend.model.Area;
import com.example.backend.model.AreaSetRequest;
import com.example.backend.service.ApiShopService;
import com.example.backend.service.PainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PaintCotroler {

    private PainService painService;
    private ApiShopService apiShopService;
    private ActualRequest actualRequest;

    public PaintCotroler(PainService painService, ApiShopService apiShopService,ActualRequest actualRequest) {
        this.apiShopService = apiShopService;
        this.painService = painService;
        this.actualRequest = actualRequest;
    }

    @PostMapping("/sendAreaSet")
    public ResponseEntity<?> sendAreSet(@RequestBody AreaSetRequest areaSetRequest) {
        ResponseEntity<?> responseEntity = painService.processPainData(areaSetRequest.getAreas());
        Double meters = (Double) responseEntity.getBody();
        System.out.println("Metry do pomalowania: " + meters);
        actualRequest.addValue("meters",meters);
        System.out.println("Color: " + areaSetRequest.getColor());
       return apiShopService.requestToApiShop(meters,areaSetRequest.getColor());
    }
}
