package com.photolab.model;

import com.photolab.model.enums.StatusOrdering;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ordering {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Masters master;
    @ManyToOne(fetch = FetchType.LAZY)
    private Users client;
    @ManyToOne(fetch = FetchType.LAZY)
    private Notes note;
    private String date;
    private String time;
    @Enumerated(EnumType.STRING)
    private StatusOrdering statusOrdering;

    public Ordering(Masters master, Users client, Notes note, String date, String time) {
        this.master = master;
        this.client = client;
        this.note = note;
        this.date = date;
        this.time = time;
        this.statusOrdering = StatusOrdering.WAITING;
    }
}
