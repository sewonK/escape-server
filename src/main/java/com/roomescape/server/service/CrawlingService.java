package com.roomescape.server.service;

import com.roomescape.server.model.ReservationDto;
import com.roomescape.server.model.StoreDto;
import com.roomescape.server.model.ThemeDto;

import java.time.LocalDate;
import java.util.List;

public interface CrawlingService {
    List<StoreDto> getStore();

    List<ThemeDto> getTheme();

    List<ReservationDto> getAllReservation(LocalDate fromDate, LocalDate toDate);

    List<ReservationDto> getReservationByThemeId(String themeId, LocalDate fromDate, LocalDate toDate);
}
