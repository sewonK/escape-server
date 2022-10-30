package com.roomescape.server.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
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
