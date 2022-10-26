package com.roomescape.server.entity;

import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@ToString
public class EscapeCafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESCAPECAFE_ID")
    private Long id;

    private String name;

    private String url;
}
