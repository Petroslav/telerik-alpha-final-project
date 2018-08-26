package com.alpha.marketplace.repositories;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.repositories.base.ExtensionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ExtensionRepositoryImpl implements ExtensionRepository {

    private SessionFactory session;

    @Autowired
    public ExtensionRepositoryImpl(SessionFactory session) {
        this.session = session;
    }

    @Override
    public List<Extension> getAll() {
        List<Extension> extensions = null;
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            extensions = sess.createQuery("FROM Extension", Extension.class).list();

            sess.getTransaction().commit();
            System.out.println("Extensions retrieved successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return extensions;
    }


    @Override
    public Extension getById(int id) {
        Extension extension = null;
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            extension = sess.get(Extension.class, (long) id);
            //TODO ask about casting good practices

            sess.getTransaction().commit();
            System.out.println("Extension " + extension.getName() + " with ID = " + extension.getId() + " retrieved successfully");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return extension;
    }

    @Override
    public Extension getByName(String name) {
        Extension extension = null;
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            extension = (Extension) sess.createQuery("FROM Extension WHERE name = :inputName")
                    .setParameter("inputName", name);

            sess.getTransaction().commit();
            System.out.println("Extension " + extension.getName() + " retrieved successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return extension;
    }

    @Override
    public List<Extension> getByDownloads(int downloads) {
        List<Extension> extensions = null;
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            extensions = sess.createQuery("FROM Extension WHERE downloads = :inputDownloads", Extension.class)
                    .setParameter("inputDownloads", downloads)
                    .list();

            sess.getTransaction().commit();
            System.out.println("Extensions retrieved successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return extensions;
    }

    @Override
    public List<Extension> getByUploadDate(Date date) {
        List<Extension> extensions = null;
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            extensions = sess.createQuery("FROM Extension WHERE addedOn = :inputDate", Extension.class)
                    .setParameter("inputDate", date)
                    .list();

            sess.getTransaction().commit();
            System.out.println("Extensions retrieved successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return extensions;
    }

    @Override
    public List<Extension> getByCommitDate(Date date) {
        //TODO implement commit date search
        return null;
    }

    @Override
    public boolean save(Extension extension) {
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            sess.save(extension);

            sess.getTransaction().commit();
            System.out.println("Extension " + extension.getName() + " saved successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Extension extension) {
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            sess.update(extension);

            sess.getTransaction().commit();
            System.out.println("Extension " + extension.getName() + " updated successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(int id) {
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            sess.delete(getById(id));

            sess.getTransaction().commit();
            System.out.println("Extension deleted successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return true;
    }
}
