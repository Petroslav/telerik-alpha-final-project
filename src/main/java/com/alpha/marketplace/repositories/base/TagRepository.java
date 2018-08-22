package com.alpha.marketplace.repositories.base;

import com.alpha.marketplace.models.Tag;

import java.util.List;

public interface TagRepository {

    List<Tag> findAll();
    Tag findByName(String name);
    Tag findById(int id);
    boolean saveTag(Tag tag);
    boolean updateTag(Tag tag);
    boolean deleteTag(Tag tag);
    boolean deleteTagById(int id);
    boolean deleteTagByName(String tagName);

}
