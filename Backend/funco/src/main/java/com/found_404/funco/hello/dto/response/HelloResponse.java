package com.found_404.funco.hello.dto.response;

import lombok.Builder;

@Builder
public record HelloResponse(
        String message
) {
}
