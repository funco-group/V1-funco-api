package com.found_404.funco.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // spring data jpa class projection -> 생성자의 파라미터 이름으로 매칭
public class Ticker {
    private String ticker;
}
