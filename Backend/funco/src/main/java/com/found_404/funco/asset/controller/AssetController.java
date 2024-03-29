package com.found_404.funco.asset.controller;

import com.found_404.funco.asset.dto.response.CashResponse;
import com.found_404.funco.asset.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @GetMapping("/cash")
    public ResponseEntity<CashResponse> getMemberCash(){
        // 임시로 설정한 memberId
        long memberId = 1L;

        return ResponseEntity.ok(assetService.getMemberCash(memberId));
    }

}
