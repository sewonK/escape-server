package com.roomescape.server.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Theme {
    @Id
    @Column(name = "THEME_ID", nullable = false)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private Store store;

    public Long getId() {
        return id;
    }

    public Long getStoreId() {
        return store.getId();
    }
}
