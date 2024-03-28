package com.found_404.funco.trade.cryptoPrice.jsonObject;

import lombok.Getter;

import java.util.List;

@Getter
public class TypeCodes {
    private final String type = "trade";
    private final List<String> codes; // market

    public TypeCodes(List<String> codes) {
        this.codes = codes;
    }
}