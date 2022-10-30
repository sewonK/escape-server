package com.roomescape.server.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
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
}
