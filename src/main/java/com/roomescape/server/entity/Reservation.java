package com.roomescape.server.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private Long id;

    private LocalDateTime time;

    private boolean possibleFlag;

    @ManyToOne
    @JoinColumn(name = "THEME_ID")
    private Theme theme;

    public Long getId() {
        return id;
    }
}
