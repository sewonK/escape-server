package com.roomescape.server.service;

import com.roomescape.server.model.StoreDto;

import java.util.List;

public interface CrawlingService {
    List<StoreDto> getStore();
}
