package com.found_404.funco.trade.cryptoPrice.jsonObject;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Ticket {
    private final String ticket = UUID.randomUUID().toString();
}
