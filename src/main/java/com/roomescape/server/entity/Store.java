package com.roomescape.server.entity;

import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@ToString
public class Store {
    @Id
    @Column(name = "STORE_ID", nullable = false)
    private Long id;

    private String name;

    private String location;

    private String tel;

    @ManyToOne
    @JoinColumn(name = "ESCAPECAFE_ID")
    private EscapeCafe escapeCafe;
}
