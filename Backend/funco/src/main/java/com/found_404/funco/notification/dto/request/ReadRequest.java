package com.found_404.funco.notification.dto.request;

import java.util.List;

public record ReadRequest(
        List<Long> readIds
) {
}
