package com.alpha.marketplace.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private int id;

    @Column(name = "tag_name", unique = true)
    private String name;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER,
            mappedBy = "tags")
    private List<Extension> taggedExtensions;

    public Tag(){}

    public Tag(String name){
        this.name = name;
        taggedExtensions = new ArrayList<>();
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return getId() == tag.getId();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }
}
