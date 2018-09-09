package com.alpha.marketplace.repositories;

import com.alpha.marketplace.models.Properties;
import com.alpha.marketplace.repositories.base.PropertiesRepository;
import com.alpha.marketplace.utils.Utils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PropertiesRepositoryImpl implements PropertiesRepository {

    private SessionFactory session;

    @Autowired
    public PropertiesRepositoryImpl(SessionFactory sessionFactory) {
        this.session = sessionFactory;
        if(Utils.properties == null) Utils.properties = get();
    }

    @Override
    public Properties get() {
        Properties properties = null;
        try (Session sess = session.openSession()) {
            sess.beginTransaction();
            properties = sess.get(Properties.class, (short)1);
            sess.getTransaction().commit();
            System.out.println("Properties retrieved successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return properties;

    }

    @Override
    public void update() {
        try (Session sess = session.openSession()) {
            sess.beginTransaction();
            sess.update(Utils.properties);
            sess.getTransaction().commit();
            System.out.println("Properties updated successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
