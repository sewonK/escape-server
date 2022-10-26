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
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "THEME_ID", nullable = false)
    private Long id;

    private String name;

    private int price;

    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private Store store;
}
