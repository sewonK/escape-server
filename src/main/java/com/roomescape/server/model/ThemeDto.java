package com.roomescape.server.model;

import com.roomescape.server.entity.Store;
import com.roomescape.server.entity.Theme;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ThemeDto {
    private Long id;
    private String name;
    private Long storeId;

    public Theme toEntity(Store store) {
        return new Theme(id, name, store);
    }
}
