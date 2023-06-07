package com.photolab.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Notes> notes;

    public Category(String name) {
        this.name = name;
        notes = new ArrayList<>();
    }

    public void addNote(Notes note) {
        notes.add(note);
        note.setCategory(this);
    }

    public void removeNote(Notes note) {
        notes.remove(note);
        note.setCategory(null);
    }
}
