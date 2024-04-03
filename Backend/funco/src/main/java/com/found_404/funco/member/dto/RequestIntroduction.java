package com.found_404.funco.member.dto;

import jakarta.validation.constraints.NotBlank;

public record RequestIntroduction(
        @NotBlank
        String introduction
) {
}
