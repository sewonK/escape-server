package com.roomescape.server.entity;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@ToString
public class EscapeCafe {
    @Id
    @Column(name = "ESCAPECAFE_ID")
    private Long id;

    private String name;

    private String url;
}
