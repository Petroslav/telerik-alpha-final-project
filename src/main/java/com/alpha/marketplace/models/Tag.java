package com.alpha.marketplace.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private int id;

    @Column(name = "tag_name")
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Extension> taggedExtensions;

    public Tag(){}

    public Tag(String name, List<Extension> taggedExtensions) {
        this.name = name;
        this.taggedExtensions = taggedExtensions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Extension> getTaggedExtensions() {
        return taggedExtensions;
    }

    public void setTaggedExtensions(List<Extension> taggedExtensions) {
        this.taggedExtensions = taggedExtensions;
    }
}
