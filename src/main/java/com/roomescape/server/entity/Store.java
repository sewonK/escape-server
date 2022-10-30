package com.roomescape.server.entity;

import javax.persistence.*;

@Entity
@Table
public class Store {
    @Id
    @Column(name = "STORE_ID", nullable = false)
    private Long id;

    private String name;

    private String location;

    private String tel;

    @Enumerated(EnumType.STRING)
    private EscapeCafe escapeCafe;
}
