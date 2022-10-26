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
public class EscapeCafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESCAPECAFE_ID")
    private Long id;

    private String name;

    private String url;
}
