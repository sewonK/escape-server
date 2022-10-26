package com.roomescape.server.entity;

import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@ToString
public class Theme {
    @Id
    @Column(name = "THEME_ID", nullable = false)
    private Long id;

    private String name;

    private int price;

    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private Store store;
}
