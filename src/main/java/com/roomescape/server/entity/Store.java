package com.roomescape.server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STORE_ID", nullable = false)
    private Long id;

    private String name;

    private String location;

    private String tel;

    @ManyToOne
    @JoinColumn(name = "ESCAPECAFE_ID")
    private EscapeCafe escapeCafe;
}
