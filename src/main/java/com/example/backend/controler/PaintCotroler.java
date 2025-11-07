package com.example.backend.controler;


import com.example.backend.model.Area;
import com.example.backend.service.ApiShopService;
import com.example.backend.service.PainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PaintCotroler {

    PainService painService;
    ApiShopService apiShopService;

    public PaintCotroler(PainService painService, ApiShopService apiShopService) {
        this.apiShopService = apiShopService;
        this.painService = painService;
    }

    @PostMapping("/sendAreaSet")
    public ResponseEntity<?> sendAreSet(@RequestBody List<Area> areaList) {
        ResponseEntity<?> responseEntity = painService.processPainData(areaList);
        return responseEntity;
    }
}
