package com.roomescape.server.model;

import com.roomescape.server.entity.EscapeCafe;
import com.roomescape.server.entity.Store;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreDto {
    private Long id;
    private String name;
    private String location;
    private String tel;
    private EscapeCafe escapeCafe;

    public Store toEntity() {
        return new Store(id, name, location, tel, escapeCafe);
    }
}
