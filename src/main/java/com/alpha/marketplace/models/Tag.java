package com.alpha.marketplace.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tags")
class Tag {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private int id;

    @Column(name = "tag_name")
    private String name;

    @ManyToMany( cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "tagged_extensions",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "extension_id")
    )
    List<Extension> taggedExtensions;
}
