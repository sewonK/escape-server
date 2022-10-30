package com.roomescape.server.service;

import com.roomescape.server.model.StoreDto;
import com.roomescape.server.model.ThemeDto;

import java.util.List;

public interface CrawlingService {
    List<StoreDto> getStore();

    List<ThemeDto> getTheme();
}
