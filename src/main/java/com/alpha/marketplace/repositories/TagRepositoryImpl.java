package com.alpha.marketplace.repositories;

import com.alpha.marketplace.models.Tag;
import com.alpha.marketplace.repositories.base.TagRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final SessionFactory session;

    @Autowired
    public TagRepositoryImpl(SessionFactory session){
        this.session = session;
    }

    @Override
    public List<Tag> findAll() {
        List<Tag> tags = null;
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            tags = sess.createQuery("FROM Tag", Tag.class).list();
            sess.getTransaction().commit();
            System.out.println("Tags retrieved successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return tags;
    }

    @Override
    public Tag findByName(String name) {
        List<Tag> tags = null;
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            tags = sess.createQuery("FROM Tag WHERE name = :name", Tag.class)
                    .setParameter("name", name)
                    .list();
            sess.getTransaction().commit();
            System.out.println("Tag " + name + " retrieved successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return tags == null ? null : tags.isEmpty() ? null : tags.get(0);
    }

    @Override
    public Tag findById(int id) {
        Tag tag = null;
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            tag = sess.get(Tag.class, id);
            sess.getTransaction().commit();
            System.out.println("Tag " + tag.getName() + " retrieved successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return tag;
    }

    @Override
    public boolean saveTag(Tag tag) {
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            sess.save(tag);
            sess.getTransaction().commit();
            System.out.println("Tag " + tag.getName() + " saved successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateTag(Tag tag) {
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            sess.update(tag);
            sess.getTransaction().commit();
            System.out.println("Tag " + tag.getName() + " updated successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteTag(Tag tag) {
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            sess.delete(tag);
            sess.getTransaction().commit();
            System.out.println("Tag " + tag.getName() + " deleted successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteTagById(int id) {
        return deleteTag(findById(id));
    }

    @Override
    public boolean deleteTagByName(String tagName) {
        return deleteTag(findByName(tagName));
    }
}
