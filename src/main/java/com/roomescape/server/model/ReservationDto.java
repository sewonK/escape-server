package com.roomescape.server.model;

import com.roomescape.server.entity.Reservation;
import com.roomescape.server.entity.Theme;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationDto {
    private Long id;
    private LocalDateTime time;
    private boolean possibleFlag;
    private Long themeId;

    public Reservation toEntity(Theme theme) {
        return new Reservation(id, time, possibleFlag, theme);
    }
}
